package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import javax.inject.Inject

interface IsFavoriteUseCase {
    suspend operator fun invoke(characterId: Long): Boolean
}

class IsFavoriteUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : IsFavoriteUseCase {
    override suspend operator fun invoke(characterId: Long): Boolean {
        return repository.isFavorite(characterId)
    }
}