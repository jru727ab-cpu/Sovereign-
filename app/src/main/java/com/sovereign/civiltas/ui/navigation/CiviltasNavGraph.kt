package com.sovereign.civiltas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sovereign.civiltas.ui.screens.*
import com.sovereign.civiltas.ui.viewmodel.GameViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Upgrades : Screen("upgrades")
    object Skills : Screen("skills")
    object Quests : Screen("quests")
    object Settings : Screen("settings")
}

@Composable
fun CiviltasNavGraph() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Upgrades.route) {
            UpgradesScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Skills.route) {
            SkillsScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Quests.route) {
            QuestsScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(viewModel = viewModel, navController = navController)
        }
    }
}
