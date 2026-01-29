package com.example.rickandmorty.data.api

import com.example.paterns.data.CharacterDto
import com.example.rickandmorty.data.api.dto.CharacterResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): Response<CharacterResponseDto>

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Long
    ): Response<CharacterDto>

    @GET("character")
    suspend fun searchCharacters(
        @Query("name") name: String,
        @Query("page") page: Int = 1
    ): Response<CharacterResponseDto>

    @GET("character")
    suspend fun getCharactersWithFilters(
        @Query("page") page: Int = 1,
        @Query("status") status: String? = null,
        @Query("gender") gender: String? = null,
        @Query("species") species: String? = null
    ): Response<CharacterResponseDto>
}
