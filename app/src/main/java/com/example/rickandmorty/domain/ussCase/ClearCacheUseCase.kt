package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.ResultModel
import javax.inject.Inject

interface ClearCacheUseCase {
    suspend operator fun invoke(): ResultModel<Unit>
}

class ClearCacheUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : ClearCacheUseCase {
    override suspend operator fun invoke(): ResultModel<Unit> {
        return try {
            repository.clearCache()
            ResultModel.Success(Unit)
        } catch (e: Exception) {
            ResultModel.Error(e)
        }
    }
}