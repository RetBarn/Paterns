package com.example.rickandmorty.domain.domainModel

data class CharacterDetailsDomainModel(
    val id: Long,
    val name: String,
    val status: CharactersStatus,
    val species: String,
    val type: String,
    val gender: CharactersGender,
    val origin: CharacterLocationDomainModel,
    val location: CharacterLocationDomainModel,
    val episodes: List<Int>,
    val isFavorite: Boolean = false
)
