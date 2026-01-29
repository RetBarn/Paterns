package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import javax.inject.Inject
import com.example.rickandmorty.domain.domainModel.ResultModel


interface SaveCharactersUseCase {
    suspend operator fun invoke(characters: List<AllCharacterDomainModel>): ResultModel<Unit>
}

class SaveCharactersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : SaveCharactersUseCase {
    override suspend operator fun invoke(characters: List<AllCharacterDomainModel>): ResultModel<Unit> {
        return repository.saveCharacters(characters)
    }
}