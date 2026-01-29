package com.example.rickandmorty.domain.ussCase


import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import javax.inject.Inject

interface GetCachedCharactersUseCase {
    suspend operator fun invoke(page: Int = 1): ResultModel<List<AllCharacterDomainModel>>
}

class GetCachedCharactersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCachedCharactersUseCase {

    override suspend operator fun invoke(page: Int): ResultModel<List<AllCharacterDomainModel>> {
        return repository.getCachedCharacters(page)
    }
}