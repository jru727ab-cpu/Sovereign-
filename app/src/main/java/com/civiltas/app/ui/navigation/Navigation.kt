package com.civiltas.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.civiltas.app.ui.screens.*

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Mining : Screen("mining", "Mining", Icons.Filled.Terrain)
    object Building : Screen("building", "Build", Icons.Filled.Construction)
    object DailyTasks : Screen("daily", "Tasks", Icons.Filled.CheckCircle)
    object Wallet : Screen("wallet", "Wallet", Icons.Filled.AccountBalanceWallet)
    object Diagnostics : Screen("diagnostics", "Logs", Icons.Filled.BugReport)
}

private val bottomNavItems = listOf(
    Screen.Home,
    Screen.Mining,
    Screen.Building,
    Screen.DailyTasks,
    Screen.Wallet
)

@Composable
fun CiviltasNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
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
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            contentPadding = innerPadding
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Mining.route) { MiningScreen() }
            composable(Screen.Building.route) { BuildingScreen() }
            composable(Screen.DailyTasks.route) { DailyTasksScreen() }
            composable(Screen.Wallet.route) { WalletScreen() }
            composable(Screen.Diagnostics.route) { DiagnosticsScreen() }
        }
    }
}
