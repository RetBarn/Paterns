package com.example.rickandmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.manager.SharedFavoritesManager
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import com.example.rickandmorty.domain.ussCase.GetFavoritesStreamUseCase
import com.example.rickandmorty.domain.ussCase.GetFavoritesUseCase
import com.example.rickandmorty.domain.ussCase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favorites: List<AllCharacterDomainModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false
)

class FavoritesViewModel @Inject constructor(
    private val favorite: GetFavoritesUseCase,
    private val favoriteStream: GetFavoritesStreamUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val favoritesManager: SharedFavoritesManager  // ← ДОБАВИТЬ
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    val favoritesStream: Flow<List<AllCharacterDomainModel>> = favoriteStream()

    init {
        loadFavorites()
        setupFavoritesStream()
        setupFavoritesManagerListener()  // ← ДОБАВИТЬ
    }

    private fun setupFavoritesManagerListener() {
        viewModelScope.launch {
            favoritesManager.favoriteUpdates.collect { update ->
                if (!update.isFavorite) {
                    // Если убрали из избранного - обновляем список
                    loadFavorites()
                }
            }
        }
    }

    private fun setupFavoritesStream() {
        viewModelScope.launch {
            favoritesStream.collect { favorites ->
                _uiState.update {
                    it.copy(
                        favorites = favorites,
                        isEmpty = favorites.isEmpty()
                    )
                }
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = favorite()

                when (result) {
                    is ResultModel.Success -> {
                        _uiState.update {
                            it.copy(
                                favorites = result.data,
                                isLoading = false,
                                isEmpty = result.data.isEmpty()
                            )
                        }
                    }

                    is ResultModel.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Ошибка загрузки избранного"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Неизвестная ошибка"
                    )
                }
            }
        }
    }

    fun removeFromFavorites(characterId: Long) {
        viewModelScope.launch {
            try {
                val currentFavorites = _uiState.value.favorites
                val updatedFavorites = currentFavorites.filter { it.id != characterId }

                _uiState.update {
                    it.copy(favorites = updatedFavorites)
                }

                toggleFavorite(characterId)

            } catch (e: Exception) {
                loadFavorites()
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun retry() {
        loadFavorites()
    }
}