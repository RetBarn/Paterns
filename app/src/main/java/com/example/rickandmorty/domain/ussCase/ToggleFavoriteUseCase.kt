package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import javax.inject.Inject
import com.example.rickandmorty.domain.domainModel.ResultModel

interface ToggleFavoriteUseCase {
    suspend operator fun invoke(characterId: Long): ResultModel<Boolean>
}

class ToggleFavoriteUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : ToggleFavoriteUseCase {
    override suspend operator fun invoke(characterId: Long): ResultModel<Boolean> {
        return try {
            val result = repository.toggleFavorite(characterId)
            ResultModel.Success(result)
        } catch (e: Exception) {
            ResultModel.Error(e)
        }
    }
}