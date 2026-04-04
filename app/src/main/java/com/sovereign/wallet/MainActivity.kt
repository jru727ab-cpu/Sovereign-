package com.sovereign.wallet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sovereign.wallet.ui.navigation.Screen
import com.sovereign.wallet.ui.navigation.bottomNavItems
import com.sovereign.wallet.ui.screens.*
import com.sovereign.wallet.ui.theme.SovereignTheme
import com.sovereign.wallet.viewmodels.WalletViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val walletViewModel: WalletViewModel = viewModel()
            val isDarkMode by walletViewModel.isDarkMode.collectAsState()
            val isUnlocked by walletViewModel.isUnlocked.collectAsState()
            val isPinEnabled by walletViewModel.isPinEnabled.collectAsState()
            val isBiometricEnabled by walletViewModel.isBiometricEnabled.collectAsState()

            SovereignTheme(darkTheme = isDarkMode) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (!isUnlocked && (isPinEnabled || isBiometricEnabled)) {
                        LockScreen(viewModel = walletViewModel)
                    } else {
                        MainApp(walletViewModel = walletViewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainApp(walletViewModel: WalletViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show bottom bar only on main tabs, not on restore screen
    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Wallet.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Wallet.route) {
                WalletScreen(
                    viewModel = walletViewModel,
                    onShowRestore = { navController.navigate(Screen.Restore.route) }
                )
            }
            composable(Screen.SendReceive.route) {
                SendReceiveScreen(viewModel = walletViewModel)
            }
            composable(Screen.BugCleaner.route) {
                BugCleanerScreen(viewModel = walletViewModel)
            }
            composable(Screen.AIHelper.route) {
                AIHelperScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = walletViewModel)
            }
            composable(Screen.Restore.route) {
                RestoreScreen(
                    viewModel = walletViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
