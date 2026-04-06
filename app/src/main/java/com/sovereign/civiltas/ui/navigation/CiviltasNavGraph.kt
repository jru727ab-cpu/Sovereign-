package com.sovereign.civiltas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sovereign.civiltas.ui.screens.*
import com.sovereign.civiltas.ui.viewmodel.GameViewModel

sealed class AppScreen(val route: String) {
    object Home : AppScreen("home")
    object Upgrades : AppScreen("upgrades")
    object Skills : AppScreen("skills")
    object Quests : AppScreen("quests")
    object Settings : AppScreen("settings")
}

@Composable
fun CiviltasNavGraph() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = AppScreen.Home.route) {
        composable(AppScreen.Home.route) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable(AppScreen.Upgrades.route) {
            UpgradesScreen(viewModel = viewModel, navController = navController)
        }
        composable(AppScreen.Skills.route) {
            SkillsScreen(viewModel = viewModel, navController = navController)
        }
        composable(AppScreen.Quests.route) {
            QuestsScreen(viewModel = viewModel, navController = navController)
        }
        composable(AppScreen.Settings.route) {
            SettingsScreen(viewModel = viewModel, navController = navController)
        }
    }
}
