package com.example.rickandmorty.data.database.converters

import androidx.room.TypeConverter

class EpisodesConverter {
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.split(",")
            ?.mapNotNull { it.trim().toIntOrNull() }
            ?.takeIf { it.isNotEmpty() }
    }
}