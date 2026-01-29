package com.example.rickandmorty.data.api.dto

import com.example.paterns.data.CharacterDto
import com.example.rickandmorty.domain.domainModel.PageInfoDomainModel
import com.example.rickandmorty.domain.domainModel.ResponseDomainModel
import com.google.gson.annotations.SerializedName

data class CharacterResponseDto(
    @SerializedName("info")
    val info: PageInfoDto,

    @SerializedName("results")
    val results: List<CharacterDto>
){
    fun toDomain(): ResponseDomainModel {
        return ResponseDomainModel(
            info = info.toDomain(),
            result = results.map { it.toDomain() }
        )
    }
}