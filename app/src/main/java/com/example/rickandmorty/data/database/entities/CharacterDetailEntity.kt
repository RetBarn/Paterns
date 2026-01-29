package com.example.rickandmorty.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmorty.domain.domainModel.CharactersGender
@Entity(tableName = "character_details")
data class CharacterDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "gender")
    val gender: CharactersGender,

    @ColumnInfo(name = "origin_name")
    val originName: String,

    @ColumnInfo(name = "origin_id")
    val originId: Int,

    @ColumnInfo(name = "location_name")
    val locationName: String,

    @ColumnInfo(name = "location_id")
    val locationId: Int,

    @ColumnInfo(name = "episodes")
    val episodes: String,

    @ColumnInfo(name = "is_detail_cached")
    val isDetailCached: Boolean = false,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
)