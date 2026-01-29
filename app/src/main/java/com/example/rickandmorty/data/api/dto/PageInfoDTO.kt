package com.example.rickandmorty.data.api.dto

import com.example.rickandmorty.domain.domainModel.PageInfoDomainModel
import com.google.gson.annotations.SerializedName

data class PageInfoDto(
    @SerializedName("count")
    val count: Int,

    @SerializedName("pages")
    val pages: Int,

    @SerializedName("next")
    val next: String?,

    @SerializedName("prev")
    val prev: String?
){
    fun toDomain(): PageInfoDomainModel  {
        return PageInfoDomainModel(
            count = count,
            pages = pages,
            next = next,
            prev = prev
        )
    }
}