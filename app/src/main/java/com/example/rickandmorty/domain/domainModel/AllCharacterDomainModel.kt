package com.example.rickandmorty.domain.domainModel

data class AllCharacterDomainModel(
    val id: Long,
    val name: String,
    val status: CharactersStatus,
    val species: String,
    val type: String,
    val gender: CharactersGender,
    val location: CharacterLocationDomainModel,
    val origin: CharacterLocationDomainModel,
    val image: String,
    val episodes: List<Int>,
    val isFavorite: Boolean = false
)