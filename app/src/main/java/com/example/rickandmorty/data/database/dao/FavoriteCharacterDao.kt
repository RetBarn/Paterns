package com.example.rickandmorty.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.rickandmorty.data.database.entities.FavoriteCharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(character: FavoriteCharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(characters: List<FavoriteCharacterEntity>)

    @Update
    suspend fun updateFavorite(character: FavoriteCharacterEntity)

    @Delete
    suspend fun deleteFavorite(character: FavoriteCharacterEntity)

    @Query("DELETE FROM favoriteCharacter WHERE id = :characterId")
    suspend fun deleteFavoriteById(characterId: Long)

    @Query("DELETE FROM favoriteCharacter")
    suspend fun clearAllFavorites()

    @Query("SELECT * FROM favoriteCharacter WHERE id = :characterId")
    suspend fun getFavoriteById(characterId: Long): FavoriteCharacterEntity?

    @Query("SELECT * FROM favoriteCharacter ORDER BY name DESC")
    suspend fun getAllFavorites(): List<FavoriteCharacterEntity>

    @Query("SELECT * FROM favoriteCharacter ORDER BY name DESC")
    fun getAllFavoritesFlow(): Flow<List<FavoriteCharacterEntity>>

    @Query("SELECT * FROM favoriteCharacter WHERE name LIKE '%' || :query || '%'")
    suspend fun searchFavorites(query: String): List<FavoriteCharacterEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favoriteCharacter WHERE id = :characterId)")
    suspend fun isFavorite(characterId: Long): Boolean

    @Query("SELECT COUNT(*) FROM favoriteCharacter")
    suspend fun getFavoritesCount(): Int

    @Transaction
    suspend fun upsertFavorite(character: FavoriteCharacterEntity) {
        val existing = getFavoriteById(character.id)
        if (existing != null) {
            updateFavorite(character)
        } else {
            insertFavorite(character)
        }
    }

    @Transaction
    suspend fun toggleFavorite(character: FavoriteCharacterEntity): Boolean {
        val isCurrentlyFavorite = isFavorite(character.id)

        if (isCurrentlyFavorite) {
            deleteFavoriteById(character.id)
            return false
        } else {
            insertFavorite(character)
            return true
        }
    }
}