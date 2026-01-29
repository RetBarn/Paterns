package com.example.rickandmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.manager.SharedFavoritesManager
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import com.example.rickandmorty.domain.ussCase.GetCharacterDetailsUseCase
import com.example.rickandmorty.domain.ussCase.IsFavoriteUseCase
import com.example.rickandmorty.domain.ussCase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterDetailsUiState(
    val character: AllCharacterDomainModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CharacterDetailsEvent {
    data class ShowMessage(val message: String) : CharacterDetailsEvent()
    data class ShowError(val error: String) : CharacterDetailsEvent()
}

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterDetails: GetCharacterDetailsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val isFavorite: IsFavoriteUseCase,
    private val favoritesManager: SharedFavoritesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState

    private val _events = MutableSharedFlow<CharacterDetailsEvent>()
    val events: SharedFlow<CharacterDetailsEvent> = _events

    private var characterId: Long = -1

    init {
        setupFavoritesListener()
    }

    private fun setupFavoritesListener() {
        viewModelScope.launch {
            favoritesManager.favoriteUpdates.collect { update ->
                if (update.characterId == characterId) {
                    _uiState.update { state ->
                        state.character?.let { character ->
                            state.copy(character = character.copy(isFavorite = update.isFavorite))
                        } ?: state
                    }
                }
            }
        }
    }

    fun setCharacterId(id: Long) {
        characterId = id
        loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val characterResult = getCharacterDetails(characterId)

                when (characterResult) {
                    is ResultModel.Success -> {
                        val isFav = isFavorite(characterId)
                        val character = characterResult.data.copy(isFavorite = isFav)

                        _uiState.update {
                            it.copy(
                                character = character,
                                isLoading = false,
                                error = null
                            )
                        }
                    }

                    is ResultModel.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = characterResult.exception.message ?: "Персонаж не найден"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Ошибка загрузки"
                    )
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentCharacter = _uiState.value.character ?: return@launch

            try {
                val wasFavorite = currentCharacter.isFavorite
                val updatedCharacter = currentCharacter.copy(isFavorite = !wasFavorite)
                _uiState.update { it.copy(character = updatedCharacter) }

                val result = toggleFavorite(characterId)

                when (result) {
                    is ResultModel.Success -> {
                        val message = if (result.data) {
                            "Добавлено в избранное"
                        } else {
                            "Убрано из избранного"
                        }
                        _events.emit(CharacterDetailsEvent.ShowMessage(message))
                    }

                    is ResultModel.Error -> {
                        _uiState.update {
                            it.copy(character = currentCharacter)
                        }
                        _events.emit(
                            CharacterDetailsEvent.ShowError(
                                "Не удалось обновить избранное"
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _events.emit(CharacterDetailsEvent.ShowError(e.message ?: "Ошибка"))
            }
        }
    }

    fun retry() {
        loadCharacter()
    }
}