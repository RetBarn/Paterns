package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.ResponseDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import javax.inject.Inject

interface GetCharactersWithFiltersUseCase {
    suspend operator fun invoke(
        page: Int = 1,
        status: String? = null,
        gender: String? = null,
        species: String? = null
    ): ResultModel<ResponseDomainModel>
}

class GetCharactersWithFiltersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharactersWithFiltersUseCase {
    override suspend operator fun invoke(
        page: Int,
        status: String?,
        gender: String?,
        species: String?
    ): ResultModel<ResponseDomainModel> {
        return repository.getCharactersWithFilters(page, status, gender, species)
    }
}