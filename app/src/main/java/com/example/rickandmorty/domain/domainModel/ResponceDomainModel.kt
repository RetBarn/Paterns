package com.example.rickandmorty.domain.domainModel

data class ResponseDomainModel(
    val info: PageInfoDomainModel,
    val result: List<AllCharacterDomainModel>
)
