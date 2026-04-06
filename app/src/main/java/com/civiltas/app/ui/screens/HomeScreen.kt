package com.civiltas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.civiltas.app.game.IdleEngine
import com.civiltas.app.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun HomeScreen(viewModel: GameViewModel) {
    val state by viewModel.uiState.collectAsState()
    val mining = state.miningState
    val unlockedCount = state.secrets.count { it.isUnlocked }
    val totalSecrets = state.secrets.size
    val forecastPercent = if (unlockedCount == 0) null else (unlockedCount.toFloat() / totalSecrets * 100).roundToInt()

    state.offlineGainPopup?.let { gain ->
        Dialog(onDismissRequest = { viewModel.dismissOfflinePopup() }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("While you were away...", color = TextMuted, fontSize = 14.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("+${formatOre(gain)} Ore", color = Gold, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text("collected during your absence", color = TextMuted, fontSize = 12.sp)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.dismissOfflinePopup() },
                        colors = ButtonDefaults.buttonColors(containerColor = Gold)
                    ) {
                        Text("Claim", color = DeepNavy, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(DeepNavy).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Text("CIVILTAS", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Gold, letterSpacing = 4.sp)
        Text("Rebuild. Remember. Survive.", fontSize = 12.sp, color = TextMuted, letterSpacing = 2.sp)
        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ORE RESERVES", fontSize = 11.sp, color = TextMuted, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Text(formatOre(mining.oreBalance), fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Gold)
                Text("${formatOre(mining.orePerSecond)} / sec", fontSize = 14.sp, color = SkyBlue)
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.collectOre() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Gold),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("⛏  COLLECT ORE", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 2.sp)
        }

        Spacer(Modifier.height(12.dp))

        val upgradeCost = IdleEngine.upgradeCost(mining.upgradeLevel)
        val canAfford = mining.oreBalance >= upgradeCost
        OutlinedButton(
            onClick = { viewModel.purchaseUpgrade() },
            enabled = canAfford,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = if (canAfford) SkyBlue else TextMuted)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "⬆  UPGRADE DRILL  (Lv.${mining.upgradeLevel} → ${mining.upgradeLevel + 1})",
                    fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                )
                Text(
                    "Cost: ${formatOre(upgradeCost)} ore", fontSize = 12.sp,
                    color = if (canAfford) Gold else TextMuted
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚠", fontSize = 18.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "CATASTROPHE FORECAST", fontSize = 11.sp, color = TextMuted, letterSpacing = 2.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(12.dp))
                if (forecastPercent == null) {
                    Text(
                        "UNKNOWN — Unlock secrets to calibrate the forecast meter.",
                        color = DangerRed, fontSize = 13.sp
                    )
                    LinearProgressIndicator(
                        progress = { 0f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 8.dp),
                        color = DangerRed,
                        trackColor = SlateLight
                    )
                } else {
                    val color = when {
                        forecastPercent < 30 -> DangerRed
                        forecastPercent < 60 -> WarningAmber
                        else -> Emerald
                    }
                    val label = when {
                        forecastPercent < 30 -> "HIGH UNCERTAINTY — Gather more intelligence"
                        forecastPercent < 60 -> "MODERATE CLARITY — Continue researching"
                        else -> "GOOD FORECAST — Order knowledge is strong"
                    }
                    Text(label, color = color, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { forecastPercent / 100f },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = color,
                        trackColor = SlateLight
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "$unlockedCount / $totalSecrets secrets known  •  ${forecastPercent}% clarity",
                        fontSize = 11.sp, color = TextMuted, textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

fun formatOre(value: Double): String {
    return when {
        value >= 1_000_000_000 -> "%.2fB".format(value / 1_000_000_000)
        value >= 1_000_000 -> "%.2fM".format(value / 1_000_000)
        value >= 1_000 -> "%.1fK".format(value / 1_000)
        else -> "%.1f".format(value)
    }
}
