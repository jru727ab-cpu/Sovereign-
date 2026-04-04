package com.civiltas.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.civiltas.app.ui.viewmodel.WalletViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(vm: WalletViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()
    var inputAddress by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("💰 Wallet") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Disclaimer
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = "🔒 Security Notice\n\nCIVILTAS never stores, requests, or handles your private keys or seed phrases. " +
                            "Only connect your wallet via official WalletConnect links. " +
                            "Always verify transaction details in your external wallet app.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Connection status
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Connected Wallet", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = state.connectedAddress ?: "No wallet connected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (state.connectedAddress != null) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Status: ${state.connectionStatus}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Connect wallet
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Connect External Wallet", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = inputAddress,
                        onValueChange = { inputAddress = it },
                        label = { Text("Wallet Address") },
                        placeholder = { Text("0x… or bc1q…") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { vm.connectWallet(inputAddress) },
                            enabled = !state.isConnecting && inputAddress.isNotBlank(),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (state.isConnecting) "Connecting…" else "Connect")
                        }
                        if (state.connectedAddress != null) {
                            OutlinedButton(
                                onClick = { vm.disconnectWallet(); inputAddress = "" },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Disconnect")
                            }
                        }
                    }
                    Text(
                        text = "Note: Full WalletConnect integration is on the roadmap. " +
                                "Currently stub – no real transaction signing occurs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Cold wallet section
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("❄️ Cold Wallet Reference", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "Store your cold wallet address here as a read-only reference. " +
                                "Private keys must NEVER be entered into this app.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.coldWalletAddress,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Supported chains: Bitcoin, Ethereum, Polygon, BNB Chain (roadmap)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Payments section
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("💳 Payments", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "• Google Play Billing: Used for in-app purchases on Google Play Store.\n" +
                                "• Stripe (direct APK): Card payments for users who install outside Play Store.\n" +
                                "• Crypto payments: Roadmap – accept on-chain payments via supported wallets.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
