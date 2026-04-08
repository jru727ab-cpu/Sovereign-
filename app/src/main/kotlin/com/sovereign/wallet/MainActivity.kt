package com.sovereign.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sovereign.wallet.ui.screens.BugCleanerScreen
import com.sovereign.wallet.ui.theme.SovereignTheme
import com.sovereign.wallet.ui.viewmodels.BugCleanerViewModel

/**
 * MainActivity – single-activity entry point for the Sovereign Wallet app.
 *
 * Currently hosts the [BugCleanerScreen] directly. As more screens are added
 * (Home, Send/Receive, AI Agent, Settings, etc.) a NavHost will be placed here
 * to manage navigation between them.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SovereignTheme {
                val bugCleanerViewModel: BugCleanerViewModel = viewModel()
                BugCleanerScreen(viewModel = bugCleanerViewModel)
            }
        }
    }
}
