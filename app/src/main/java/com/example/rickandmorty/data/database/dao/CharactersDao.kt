package com.example.rickandmorty.data.database.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.rickandmorty.data.database.entities.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Long): CharacterEntity?

    @Query("SELECT * FROM characters ORDER BY id LIMIT :limit OFFSET :offset")
    suspend fun getCharacters(limit: Int, offset: Int): List<CharacterEntity>

    @Query("SELECT * FROM characters ORDER BY id DESC LIMIT :limit")
    suspend fun getRecentCharacters(limit: Int = 50): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE isFavorite = 1")
    suspend fun getFavoriteCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE isFavorite = 1")
    fun getFavoriteCharactersFlow(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%'")
    suspend fun searchCharacters(query: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE status = :status")
    suspend fun getCharactersByStatus(status: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE gender = :gender")
    suspend fun getCharactersByGender(gender: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE species = :species")
    suspend fun getCharactersBySpecies(species: String): List<CharacterEntity>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :characterId")
    suspend fun updateFavoriteStatus(characterId: Long, isFavorite: Boolean)

    @Query("DELETE FROM characters")
    suspend fun clearCache()

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCacheSize(): Int

    @Transaction
    suspend fun replaceAllCharacters(characters: List<CharacterEntity>) {
        clearCache()
        insertCharacters(characters)
    }

    @Transaction
    suspend fun updateCharacterFavorite(characterId: Long): Boolean {
        val character = getCharacterById(characterId)
        return if (character != null) {
            val newStatus = !character.isFavorite
            updateFavoriteStatus(characterId, newStatus)
            newStatus
        } else {
            false
        }
    }
}