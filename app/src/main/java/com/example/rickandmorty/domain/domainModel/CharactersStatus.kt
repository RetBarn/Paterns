package com.example.rickandmorty.domain.domainModel

enum class CharactersStatus {
    ALIVE,
    DEAD,
    UNKNOWN;

    companion object {
        fun fromString(value: String):CharactersStatus{
            return when (value.lowercase()){
                "alive" -> ALIVE
                "dead" -> DEAD
                else -> UNKNOWN
            }
        }
    }
}