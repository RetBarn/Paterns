package com.example.rickandmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.manager.SharedFavoritesManager
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.ResponseDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import com.example.rickandmorty.domain.ussCase.GetCharacterWithCacheUseCase
import com.example.rickandmorty.domain.ussCase.GetCharactersWithFiltersUseCase
import com.example.rickandmorty.domain.ussCase.GetFavoritesStreamUseCase
import com.example.rickandmorty.domain.ussCase.SearchCharactersUseCase
import com.example.rickandmorty.domain.ussCase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val getCharacterWithCache: GetCharacterWithCacheUseCase,
    private val searchCharacters: SearchCharactersUseCase,
    private val getCharactersWithFilters: GetCharactersWithFiltersUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val favoritesManager: SharedFavoritesManager,
    private val getFavoritesStream: GetFavoritesStreamUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CharactersListState>(CharactersListState.Loading)
    val state: StateFlow<CharactersListState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filters = MutableStateFlow(
        Filters(
            status = null,
            gender = null,
            species = null
        )
    )
    val filters: StateFlow<Filters> = _filters

    private val favorites: Flow<List<AllCharacterDomainModel>> = getFavoritesStream()

    private var currentPage = 1
    private var hasNextPage = true

    private val _allCharacters = mutableListOf<AllCharacterDomainModel>()

    init {
        loadFirstPage()
        setupFavoritesListener()
        setupFavoritesManagerListener()
    }

    private fun setupFavoritesListener() {
        viewModelScope.launch {
            favorites.collect { favoritesList ->
                val favoriteIds = favoritesList.map { it.id }.toSet()

                updateAllFavorites(favoriteIds)
            }
        }
    }

    private fun setupFavoritesManagerListener() {
        viewModelScope.launch {
            favoritesManager.favoriteUpdates.collect { update ->
                updateSingleFavorite(update.characterId, update.isFavorite)
            }
        }
    }

    private fun updateAllFavorites(favoriteIds: Set<Long>) {
        val currentState = _state.value
        if (currentState is CharactersListState.Success) {
            val updatedCharacters = currentState.characters.map { character ->
                character.copy(isFavorite = favoriteIds.contains(character.id))
            }

            _allCharacters.forEachIndexed { index, character ->
                _allCharacters[index] = character.copy(
                    isFavorite = favoriteIds.contains(character.id)
                )
            }

            _state.value = currentState.copy(characters = updatedCharacters)
        }
    }

    private fun updateSingleFavorite(characterId: Long, isFavorite: Boolean) {
        val currentState = _state.value
        if (currentState is CharactersListState.Success) {
            val updatedCharacters = currentState.characters.map { character ->
                if (character.id == characterId) {
                    character.copy(isFavorite = isFavorite)
                } else {
                    character
                }
            }
            _allCharacters.forEachIndexed { index, character ->
                if (character.id == characterId) {
                    _allCharacters[index] = character.copy(isFavorite = isFavorite)
                }
            }

            _state.value = currentState.copy(characters = updatedCharacters)
        }
    }

    fun processIntent(intent: CharactersListIntent) {
        when (intent) {
            is CharactersListIntent.LoadFirstPage -> loadFirstPage()
            is CharactersListIntent.LoadNextPage -> loadNextPage()
            is CharactersListIntent.Search -> search(intent.query)
            is CharactersListIntent.ToggleFavorite -> toggleFavoriteCharacter(intent.characterId)
            is CharactersListIntent.ClearSearch -> clearSearch()
            is CharactersListIntent.ApplyFilters -> applyFilters(
                intent.status,
                intent.gender,
                intent.species
            )
        }
    }

    private fun loadFirstPage() {
        viewModelScope.launch {
            currentPage = 1
            _allCharacters.clear()
            _state.value = CharactersListState.Loading

            val result = if (_searchQuery.value.isNotEmpty()) {
                searchCharacters(_searchQuery.value, currentPage)
            } else if (_filters.value.hasFilters()) {
                getCharactersWithFilters(
                    page = currentPage,
                    status = _filters.value.status,
                    gender = _filters.value.gender,
                    species = _filters.value.species
                )
            } else {
                getCharacterWithCache(currentPage)
            }

            handleResult(result)
        }
    }

    private fun loadNextPage() {
        if (!hasNextPage) return

        viewModelScope.launch {
            currentPage++

            val result = if (_searchQuery.value.isNotEmpty()) {
                searchCharacters(_searchQuery.value, currentPage)
            } else if (_filters.value.hasFilters()) {
                getCharactersWithFilters(
                    page = currentPage,
                    status = _filters.value.status,
                    gender = _filters.value.gender,
                    species = _filters.value.species
                )
            } else {
                getCharacterWithCache(currentPage)
            }

            handleResult(result, append = true)
        }
    }

    private fun search(query: String) {
        _searchQuery.value = query
        loadFirstPage()
    }

    private fun clearSearch() {
        _searchQuery.value = ""
        loadFirstPage()
    }

    private fun applyFilters(status: String?, gender: String?, species: String?) {
        _filters.value = Filters(status, gender, species)
        loadFirstPage()
    }

    private fun toggleFavoriteCharacter(characterId: Long) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is CharactersListState.Success) {
                val currentCharacter = currentState.characters.find { it.id == characterId }
                currentCharacter?.let {
                    val newIsFavorite = !it.isFavorite
                    updateSingleFavorite(characterId, newIsFavorite)
                }
            }

            val result = toggleFavorite(characterId)

            if (result is ResultModel.Error) {
                val currentStateAfter = _state.value
                if (currentStateAfter is CharactersListState.Success) {
                    val currentCharacter = currentStateAfter.characters.find { it.id == characterId }
                    currentCharacter?.let {
                        updateSingleFavorite(characterId, !it.isFavorite)
                    }
                }
            }
        }
    }

    private fun handleResult(result: ResultModel<ResponseDomainModel>, append: Boolean = false) {
        when (result) {
            is ResultModel.Success -> {
                val characters = result.data.result
                val info = result.data.info

                if (append) {
                    _allCharacters.addAll(characters)
                } else {
                    _allCharacters.clear()
                    _allCharacters.addAll(characters)
                }

                hasNextPage = info.next != null

                _state.value = if (_allCharacters.isEmpty()) {
                    CharactersListState.Empty
                } else {
                    CharactersListState.Success(
                        characters = _allCharacters.toList(),
                        hasNextPage = hasNextPage,
                        currentPage = currentPage
                    )
                }
            }

            is ResultModel.Error -> {
                _state.value = CharactersListState.Error(
                    result.exception.message ?: "Произошла ошибка"
                )
            }
        }
    }
}

data class Filters(
    val status: String?,
    val gender: String?,
    val species: String?
) {
    fun hasFilters(): Boolean {
        return status != null || gender != null || species != null
    }

    fun clear(): Filters = Filters(null, null, null)
}