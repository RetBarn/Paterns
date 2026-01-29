package com.example.rickandmorty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.database.entities.CharacterDetailEntity

@Dao
interface CharacterDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterDetailEntity)

    @Query("SELECT * FROM character_details WHERE id = :id")
    suspend fun getById(id: Long): CharacterDetailEntity?

    @Query("DELETE FROM character_details WHERE last_updated < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)

    @Query("UPDATE character_details SET is_detail_cached = :cached WHERE id = :id")
    suspend fun updateCacheStatus(id: Long, cached: Boolean)

    @Query("DELETE FROM character_details")
    suspend fun clearAll()
}