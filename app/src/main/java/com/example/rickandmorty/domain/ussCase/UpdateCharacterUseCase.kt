package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import javax.inject.Inject
import com.example.rickandmorty.domain.domainModel.ResultModel

interface UpdateCharacterUseCase {
    suspend operator fun invoke(character: AllCharacterDomainModel): ResultModel<Unit>
}

class UpdateCharacterUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : UpdateCharacterUseCase {
    override suspend operator fun invoke(character: AllCharacterDomainModel): ResultModel<Unit> {
        return try {
            repository.updateCharacter(character)
            ResultModel.Success(Unit)
        } catch (e: Exception) {
            ResultModel.Error(e)
        }
    }
}