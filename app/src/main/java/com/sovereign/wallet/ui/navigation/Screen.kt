package com.sovereign.wallet.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Wallet : Screen("wallet", "Wallet", Icons.Filled.AccountBalanceWallet)
    object SendReceive : Screen("send_receive", "Send/Recv", Icons.Filled.SwapHoriz)
    object BugCleaner : Screen("bug_cleaner", "Cleaner", Icons.Filled.Build)
    object AIHelper : Screen("ai_helper", "AI", Icons.Filled.Psychology)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)

    // Non-tab screens
    object Restore : Screen("restore", "Restore", Icons.Filled.AccountBalanceWallet)
    object LockScreen : Screen("lock", "Lock", Icons.Filled.Settings)
    object PinSetup : Screen("pin_setup", "PIN Setup", Icons.Filled.Settings)
}

val bottomNavItems = listOf(
    Screen.Wallet,
    Screen.SendReceive,
    Screen.BugCleaner,
    Screen.AIHelper,
    Screen.Settings
)
