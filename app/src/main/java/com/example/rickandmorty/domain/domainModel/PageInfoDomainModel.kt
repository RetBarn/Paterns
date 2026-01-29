package com.example.rickandmorty.domain.domainModel

data class PageInfoDomainModel(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)