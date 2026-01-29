package com.example.rickandmorty.presentation.ui

sealed class Screen(
    val route: String
) {
    object Characters : Screen("characters")

    object CharacterDetails : Screen("character_details/{characterId}") {
        fun createRoute(characterId: Long) = "character_details/$characterId"
    }

    object WorkManagerTest : Screen("work_manager_test")
}