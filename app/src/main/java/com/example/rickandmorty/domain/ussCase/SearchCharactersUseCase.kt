package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.ResponseDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import javax.inject.Inject

interface SearchCharactersUseCase {
    suspend operator fun invoke(
        query: String,
        page: Int = 1
    ): ResultModel<ResponseDomainModel>
}

class SearchCharactersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : SearchCharactersUseCase {
    override suspend operator fun invoke(
        query: String,
        page: Int
    ): ResultModel<ResponseDomainModel> {
        return repository.searchCharacters(query, page)
    }
}