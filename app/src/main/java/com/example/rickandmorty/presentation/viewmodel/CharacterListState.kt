package com.example.rickandmorty.presentation.viewmodel

import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel

sealed class CharactersListState {
    object Loading : CharactersListState()
    object Empty : CharactersListState()
    data class Success(
        val characters: List<AllCharacterDomainModel>,
        val hasNextPage: Boolean,
        val currentPage: Int
    ) : CharactersListState()
    data class Error(val message: String) : CharactersListState()
}