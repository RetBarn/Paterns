package com.example.rickandmorty.domain.ussCase


import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import javax.inject.Inject

interface GetCharacterDetailsUseCase {
    suspend operator fun invoke(characterId: Long): ResultModel<AllCharacterDomainModel>
}

class GetCharacterDetailsUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetCharacterDetailsUseCase {
    override suspend operator fun invoke(characterId: Long): ResultModel<AllCharacterDomainModel> {
        return repository.getCharacterById(characterId)
    }
}