package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.ResponseDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import javax.inject.Inject

interface GetCharactersUseCase {
    suspend operator fun invoke(page: Int = 1): ResultModel<ResponseDomainModel>
}

class GetCharactersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharactersUseCase {
    override suspend operator fun invoke(page: Int): ResultModel<ResponseDomainModel> {
        return repository.getCharacters(page)
    }
}