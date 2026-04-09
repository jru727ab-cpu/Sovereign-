package com.sovereign.civiltas.ui.navigation

/**
 * Defines all navigation destinations in the app.
 */
sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Mining : Screen("mining")
    data object Civilization : Screen("civilization")
    data object Arcanum : Screen("arcanum")
    data object Vault : Screen("vault")
}
