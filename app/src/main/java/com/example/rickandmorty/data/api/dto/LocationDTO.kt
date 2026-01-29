package com.example.rickandmorty.data.api.dto

import com.example.rickandmorty.domain.domainModel.CharacterLocationDomainModel
import com.google.gson.annotations.SerializedName

data class LocationDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String?
){
    fun toDomain(): CharacterLocationDomainModel {
        val id = url?.substringAfterLast("/")?.toIntOrNull() ?: -1
        return CharacterLocationDomainModel(id = id, name = name)
    }
}