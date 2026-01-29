package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetFavoritesStreamUseCase {
    operator fun invoke(): Flow<List<AllCharacterDomainModel>>
}

class GetFavoritesStreamUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetFavoritesStreamUseCase {
    override operator fun invoke(): Flow<List<AllCharacterDomainModel>> {
        return repository.getFavoritesStream()
    }
}