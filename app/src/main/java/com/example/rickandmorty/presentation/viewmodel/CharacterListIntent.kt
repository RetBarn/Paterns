package com.example.rickandmorty.presentation.viewmodel

sealed class CharactersListIntent {
    object LoadFirstPage : CharactersListIntent()
    object LoadNextPage : CharactersListIntent()
    data class Search(val query: String) : CharactersListIntent()
    data class ToggleFavorite(val characterId: Long) : CharactersListIntent()
    object ClearSearch : CharactersListIntent()
    data class ApplyFilters(
        val status: String?,
        val gender: String?,
        val species: String?
    ) : CharactersListIntent()
}