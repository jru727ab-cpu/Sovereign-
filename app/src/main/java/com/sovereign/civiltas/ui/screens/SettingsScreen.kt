package com.sovereign.civiltas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.civiltas.app.BuildConfig
import com.sovereign.civiltas.diagnostics.BugReporter
import com.sovereign.civiltas.ui.theme.*
import com.sovereign.civiltas.ui.viewmodel.GameViewModel

@Composable
fun SettingsScreen(viewModel: GameViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Scaffold(bottomBar = { BottomNavBar(navController) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CiviltasDark)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("SETTINGS", color = OreGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            // Account
            SettingsSection("ACCOUNT") {
                Text(
                    if (state.guestMode) "Playing as: Guest Sovereign" else "Logged in",
                    color = TextSecondary, fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { /* Phase 2: real login */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Sign In / Create Account (Coming Soon)", color = TextSecondary)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Sync
            SettingsSection("CLOUD SYNC") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Enable Sync", color = TextPrimary, fontSize = 14.sp)
                        Text(
                            if (BuildConfig.ONLINE_SYNC_ENABLED) "Requires account" else "Coming soon",
                            color = TextSecondary, fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = state.onlineSyncEnabled,
                        onCheckedChange = { /* Phase 2 */ },
                        enabled = BuildConfig.ONLINE_SYNC_ENABLED,
                        colors = SwitchDefaults.colors(checkedThumbColor = OreGold)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Diagnostics
            SettingsSection("DIAGNOSTICS") {
                Button(
                    onClick = { BugReporter.exportAndShare(context) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CiviltasSurface),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Export Bug Report / Logs", color = TextPrimary)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Report issues directly from the game. Logs contain no personal data.",
                    color = TextSecondary, fontSize = 11.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // Monetization
            SettingsSection("SUPPORT CIVILTAS") {
                Text(
                    "Remove Ads · VIP Subscription · Season Pass",
                    color = TextSecondary, fontSize = 13.sp
                )
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { /* Phase 2: IAP */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("View Offers (Coming Soon)", color = TextSecondary)
                }
            }

            Spacer(Modifier.height(16.dp))

            // About
            SettingsSection("ABOUT") {
                Text("CIVILTAS v${BuildConfig.VERSION_NAME}", color = TextSecondary, fontSize = 13.sp)
                Text("Offline-first idle civilization game", color = TextSecondary, fontSize = 12.sp)
                Text("Ads: ${if (BuildConfig.ADS_ENABLED) "On" else "Off (default)"}", color = TextSecondary, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CiviltasSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(title, color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}
