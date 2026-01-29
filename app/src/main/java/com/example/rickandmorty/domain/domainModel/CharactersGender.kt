package com.example.rickandmorty.domain.domainModel

enum class CharactersGender {
    MALE,
    FEMALE,
    GENDERLESS,
    UNKNOWN;

    companion object{
        fun fromString(value: String):CharactersGender{
            return when (value.lowercase()){
                "male" -> MALE
                "female" -> FEMALE
                "genderless" -> GENDERLESS
                else -> UNKNOWN
            }
        }
    }
}