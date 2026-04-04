package com.civiltas.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.civiltas.app.data.GameRepository
import com.civiltas.app.ui.screens.HomeScreen
import com.civiltas.app.ui.screens.SecretsScreen
import com.civiltas.app.ui.screens.StoreScreen
import com.civiltas.app.ui.screens.GameViewModel
import com.civiltas.app.ui.screens.GameViewModelFactory

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Secrets : Screen("secrets", "Secrets", Icons.Default.Lock)
    object Store : Screen("store", "Store", Icons.Default.ShoppingCart)
}

@Composable
fun AppNavigation(repository: GameRepository) {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Secrets, Screen.Store)
    val viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(repository))

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = androidx.compose.ui.graphics.Color(0xFF1E293B)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { HomeScreen(viewModel) }
            composable(Screen.Secrets.route) { SecretsScreen(viewModel) }
            composable(Screen.Store.route) { StoreScreen() }
        }
    }
}
