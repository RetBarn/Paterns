package com.example.paterns.data

import com.example.rickandmorty.data.api.dto.LocationDTO
import com.example.rickandmorty.domain.domainModel.CharacterLocationDomainModel
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.CharactersGender
import com.example.rickandmorty.domain.domainModel.CharactersStatus
import com.google.gson.annotations.SerializedName


data class CharacterDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("species")
    val species: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("origin")
    val origin: LocationDTO,

    @SerializedName("location")
    val location: LocationDTO,

    @SerializedName("image")
    val image: String,

    @SerializedName("episode")
    val episode: List<String>,
){
    fun toDomain(): AllCharacterDomainModel {
        return AllCharacterDomainModel(
            id = id,
            name = name,
            status = CharactersStatus.fromString(status),
            species = species,
            type = type,
            gender = CharactersGender.fromString(gender),
            location = location.toDomain(),
            origin = origin.toDomain(),
            image = image,
            episodes = episode.map { url ->
                url.substringAfterLast("/").toIntOrNull() ?: 0
            }
        )
    }
}
