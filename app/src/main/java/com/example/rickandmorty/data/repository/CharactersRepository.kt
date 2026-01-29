package com.example.rickandmorty.data.repository

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.database.dao.CharactersDao
import com.example.rickandmorty.data.database.dao.FavoriteCharacterDao
import com.example.rickandmorty.data.database.entities.toDomain
import com.example.rickandmorty.data.database.entities.toEntity
import com.example.rickandmorty.data.database.entities.toFavoriteEntity
import com.example.rickandmorty.data.manager.SharedFavoritesManager
import com.example.rickandmorty.data.worker.CharacterNotificationWorker
import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.ResponseDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val cacheDao: CharactersDao,
    private val favoriteDao: FavoriteCharacterDao,
    private val favoritesManager: SharedFavoritesManager,
    @ApplicationContext private val context: Context
) : CharactersRepository {

    override suspend fun getCharacters(page: Int): ResultModel<ResponseDomainModel> {
        val response = api.getCharacters(page)
        return if (response.isSuccessful && response.body() != null) {
            ResultModel.Success(response.body()!!.toDomain())
        } else {
            ResultModel.Error(Exception("Ошибка загрузки"))
        }
    }

    override suspend fun getCharacterById(id: Long): ResultModel<AllCharacterDomainModel> {
        val response = api.getCharacterById(id)
        return if (response.isSuccessful && response.body() != null) {
            val characterDto = response.body()!!
            val isFav = isFavorite(id)
            ResultModel.Success(characterDto.toDomain().copy(isFavorite = isFav))
        } else {
            ResultModel.Error(Exception("Персонаж не найден"))
        }
    }

    override suspend fun searchCharacters(
        query: String,
        page: Int
    ): ResultModel<ResponseDomainModel> {
        val response = api.searchCharacters(query, page)
        return if (response.isSuccessful && response.body() != null) {
            ResultModel.Success(response.body()!!.toDomain())
        } else {
            ResultModel.Error(Exception("Ничего не найдено"))
        }
    }

    override suspend fun getCharactersWithFilters(
        page: Int,
        status: String?,
        gender: String?,
        species: String?
    ): ResultModel<ResponseDomainModel> {
        val response = api.getCharactersWithFilters(page, status, gender, species)
        return if (response.isSuccessful && response.body() != null) {
            ResultModel.Success(response.body()!!.toDomain())
        } else {
            ResultModel.Error(Exception("Ошибка фильтрации"))
        }
    }

    override suspend fun getCachedCharacters(page: Int): ResultModel<List<AllCharacterDomainModel>> {
        val pageSize = 20
        val offset = (page - 1) * pageSize
        val entities = cacheDao.getCharacters(limit = pageSize, offset = offset)
        return ResultModel.Success(entities.map { it.toDomain() })
    }

    override suspend fun saveCharacters(characters: List<AllCharacterDomainModel>): ResultModel<Unit> {
        val entities = characters.map { it.toEntity() }
        cacheDao.insertCharacters(entities)
        return ResultModel.Success(Unit)
    }

    override suspend fun getFavorites(): ResultModel<List<AllCharacterDomainModel>> {
        val entities = favoriteDao.getAllFavorites()
        return ResultModel.Success(entities.map { it.toDomain() })
    }

    override fun getFavoritesStream(): Flow<List<AllCharacterDomainModel>> {
        return favoriteDao.getAllFavoritesFlow()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun updateCharacter(character: AllCharacterDomainModel) {
        cacheDao.insertCharacter(character.toEntity())
        if (character.isFavorite) {
            favoriteDao.insertFavorite(character.toFavoriteEntity())
        }
    }

    override suspend fun clearCache() {
        cacheDao.clearCache()
    }

    override suspend fun addToFavorites(characterId: Long) {
        val character = getCharacterById(characterId).getOrNull()
        character?.let {
            favoriteDao.insertFavorite(it.toFavoriteEntity())
            cacheDao.updateFavoriteStatus(characterId, true)
        }
    }

    override suspend fun removeFromFavorites(characterId: Long) {
        favoriteDao.deleteFavoriteById(characterId)
        cacheDao.updateFavoriteStatus(characterId, false)
    }

    override suspend fun isFavorite(characterId: Long): Boolean {
        return favoriteDao.isFavorite(characterId)
    }

    override suspend fun toggleFavorite(characterId: Long): Boolean {
        return try {
            val isCurrentlyFavorite = isFavorite(characterId)
            val newStatus = !isCurrentlyFavorite

            if (isCurrentlyFavorite) {
                removeFromFavorites(characterId)
            } else {
                addToFavorites(characterId)
            }

            favoritesManager.notifyFavoriteChanged(characterId, newStatus)

            newStatus
        } catch (e: Exception) {
            false
        }
    }

    override fun scheduleNotification(delayMinutes: Long) {
        val actualDelay = if (delayMinutes == 0L) {
            30 * 1000L // 30 секунд для теста
        } else {
            delayMinutes * 60 * 1000L
        }

        val notificationRequest = OneTimeWorkRequestBuilder<CharacterNotificationWorker>()
            .setInitialDelay(actualDelay, TimeUnit.MILLISECONDS)
            .addTag("character_notification")
            .build()

        WorkManager.getInstance(context).enqueue(notificationRequest)
    }

    override fun scheduleDailyNotification() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val delayMillis = calendar.timeInMillis - System.currentTimeMillis()

        val dailyRequest = OneTimeWorkRequestBuilder<CharacterNotificationWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .addTag("daily_notification")
            .build()

        WorkManager.getInstance(context).enqueue(dailyRequest)
    }

    override fun cancelAllNotifications() {
        WorkManager.getInstance(context).cancelAllWorkByTag("character_notification")
        WorkManager.getInstance(context).cancelAllWorkByTag("daily_notification")
    }
}