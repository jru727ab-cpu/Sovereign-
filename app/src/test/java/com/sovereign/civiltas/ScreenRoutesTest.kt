package com.sovereign.civiltas

import com.sovereign.civiltas.ui.navigation.Screen
import org.junit.Assert.assertEquals
import org.junit.Test

class ScreenRoutesTest {

    @Test
    fun `screen routes are unique`() {
        val screens = listOf(
            Screen.Dashboard,
            Screen.Mining,
            Screen.Civilization,
            Screen.Arcanum,
            Screen.Vault,
        )
        val routes = screens.map { it.route }
        assertEquals("All screen routes must be unique", routes.size, routes.distinct().size)
    }

    @Test
    fun `dashboard route is correct`() {
        assertEquals("dashboard", Screen.Dashboard.route)
    }

    @Test
    fun `mining route is correct`() {
        assertEquals("mining", Screen.Mining.route)
    }

    @Test
    fun `vault route is correct`() {
        assertEquals("vault", Screen.Vault.route)
    }
}
