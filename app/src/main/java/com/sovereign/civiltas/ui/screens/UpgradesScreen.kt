package com.sovereign.civiltas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sovereign.civiltas.domain.model.*
import com.sovereign.civiltas.ui.theme.*
import com.sovereign.civiltas.ui.viewmodel.GameViewModel
import java.util.Locale

@Composable
fun UpgradesScreen(viewModel: GameViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()

    val upgrades = listOf(
        oreUpgrades(state.oreUpgradeLevel),
        stoneUpgrades(state.stoneUpgradeLevel),
        energyUpgrades(state.energyUpgradeLevel)
    )

    Scaffold(bottomBar = { BottomNavBar(navController) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CiviltasDark)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("UPGRADES", color = OreGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Invest resources to scale your civilization", color = TextSecondary, fontSize = 13.sp)

            Spacer(Modifier.height(16.dp))

            upgrades.forEach { upgrade ->
                UpgradeCard(
                    upgrade = upgrade,
                    ore = state.ore,
                    stone = state.stone,
                    onBuy = { viewModel.buyUpgrade(upgrade.id) }
                )
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun UpgradeCard(upgrade: Upgrade, ore: Double, stone: Double, onBuy: () -> Unit) {
    val canAfford = ore >= upgrade.oreCost && stone >= upgrade.stoneCost
    val maxed = upgrade.level >= upgrade.maxLevel

    Card(
        colors = CardDefaults.cardColors(containerColor = CiviltasSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(upgrade.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text("Lv. ${upgrade.level}/${upgrade.maxLevel}", color = TextSecondary, fontSize = 13.sp)
            }
            Spacer(Modifier.height(4.dp))
            Text(upgrade.description, color = TextSecondary, fontSize = 13.sp)
            Text(upgrade.effect, color = OreGold, fontSize = 12.sp)
            Spacer(Modifier.height(12.dp))
            if (!maxed) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Cost:", color = TextSecondary, fontSize = 12.sp)
                        Text(
                            "⛏ ${fmtU(upgrade.oreCost)}  🪨 ${fmtU(upgrade.stoneCost)}",
                            color = if (canAfford) OreGold else CatastropheRed, fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = onBuy,
                        enabled = canAfford,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OreGold, contentColor = CiviltasDark,
                            disabledContainerColor = CiviltasBorder
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Upgrade", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Text("MAXED OUT", color = AccentGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun fmtU(v: Double) = when {
    v >= 1000 -> String.format(Locale.US, "%.1fK", v / 1000)
    else -> String.format(Locale.US, "%.0f", v)
}
