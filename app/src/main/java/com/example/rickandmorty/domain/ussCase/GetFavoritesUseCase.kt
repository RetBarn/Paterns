package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import javax.inject.Inject

interface GetFavoritesUseCase {
    suspend operator fun invoke(): ResultModel<List<AllCharacterDomainModel>>
}

class GetFavoritesUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetFavoritesUseCase {
    override suspend operator fun invoke(): ResultModel<List<AllCharacterDomainModel>> {
        return repository.getFavorites()
    }
}