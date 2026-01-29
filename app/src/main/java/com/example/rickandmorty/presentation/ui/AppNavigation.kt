package com.example.rickandmorty.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Characters.route
    ) {
        composable(
            route = Screen.Characters.route
        ) {
            CharactersListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(
                        Screen.CharacterDetails.createRoute(characterId)
                    )
                },
                onNotificationClick = {
                    navController.navigate(Screen.WorkManagerTest.route)
                }
            )
        }

        composable(
            route = Screen.CharacterDetails.route,
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getLong("characterId") ?: -1L

            CharacterDetailsScreen(
                characterId = characterId,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.WorkManagerTest.route
        ) {
            NotificationScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}