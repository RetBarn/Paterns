package com.example.rickandmorty.data.database.converters

import androidx.room.TypeConverter
import com.example.rickandmorty.domain.domainModel.CharactersGender

class GenderConverter {
    @TypeConverter
    fun genderToString(gender: CharactersGender)= gender.name

    @TypeConverter
    fun stringToGender(value: String)= enumValueOf<CharactersGender>(value)
}