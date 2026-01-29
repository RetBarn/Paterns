package com.example.rickandmorty.data.database.converters

import androidx.room.TypeConverter
import com.example.rickandmorty.domain.domainModel.CharactersStatus

class StatusConverter {
    @TypeConverter
    fun statusToString(value: CharactersStatus) = value.name

    @TypeConverter
    fun stringToStatus(value: String) = enumValueOf<CharactersStatus>(value)
}