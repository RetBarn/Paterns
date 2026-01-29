package com.example.rickandmorty.domain

import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.ResponseDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import kotlinx.coroutines.flow.Flow
interface CharactersRepository {
    suspend fun getCharacters(page: Int = 1): ResultModel<ResponseDomainModel>

    suspend fun getCharacterById(id: Long): ResultModel<AllCharacterDomainModel>

    suspend fun searchCharacters(query: String, page: Int = 1): ResultModel<ResponseDomainModel>

    suspend fun getCharactersWithFilters(
        page: Int = 1,
        status: String? = null,
        gender: String? = null,
        species: String? = null
    ): ResultModel<ResponseDomainModel>

    suspend fun getCachedCharacters(page: Int = 1): ResultModel<List<AllCharacterDomainModel>>

    suspend fun saveCharacters(characters: List<AllCharacterDomainModel>): ResultModel<Unit>

    suspend fun clearCache()

    suspend fun addToFavorites(characterId: Long)

    suspend fun removeFromFavorites(characterId: Long)

    suspend fun getFavorites(): ResultModel<List<AllCharacterDomainModel>>

    fun getFavoritesStream(): Flow<List<AllCharacterDomainModel>>

    suspend fun isFavorite(characterId: Long): Boolean

    suspend fun updateCharacter(character: AllCharacterDomainModel)

    suspend fun toggleFavorite(characterId: Long): Boolean

    fun scheduleNotification(delayMinutes: Long)
    fun scheduleDailyNotification()
    fun cancelAllNotifications()
}