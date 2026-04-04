package com.sovereign.civiltas.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sovereign.civiltas.domain.engine.CatastropheEngine
import com.sovereign.civiltas.domain.engine.QuestEngine
import com.sovereign.civiltas.ui.navigation.Screen
import com.sovereign.civiltas.ui.theme.*
import com.sovereign.civiltas.ui.viewmodel.GameViewModel
import java.util.Locale

@Composable
fun HomeScreen(viewModel: GameViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val offlineGains by viewModel.offlineGains.collectAsState()

    // Offline gains dialog
    offlineGains?.let { gains ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissOfflineGains() },
            title = { Text("Welcome Back, Sovereign!") },
            text = {
                Column {
                    Text("You were away for ${formatDuration(gains.elapsedSeconds)}.")
                    Spacer(Modifier.height(8.dp))
                    Text("⛏ Ore gained: +${fmt(gains.oreGained)}")
                    Text("🪨 Stone gained: +${fmt(gains.stoneGained)}")
                    Text("📚 Knowledge: +${fmt(gains.knowledgeGained)}")
                    Text("✨ XP gained: +${gains.xpGained}")
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissOfflineGains() }) { Text("Collect!") }
            }
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CiviltasDark)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "CIVILTAS",
                        color = OreGold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Level ${state.level} Sovereign",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("⭐ ${state.skillPoints} SP", color = OreGold, fontSize = 13.sp)
                    Text("✨ ${state.xp} XP", color = KnowledgeBlue, fontSize = 11.sp)
                }
            }

            Spacer(Modifier.height(12.dp))

            // XP Progress bar
            val xpForNextLevel = com.sovereign.civiltas.domain.engine.ResourceEngine.xpForLevel(state.level + 1)
            val xpForCurrentLevel = com.sovereign.civiltas.domain.engine.ResourceEngine.xpForLevel(state.level)
            val xpProgress = if (xpForNextLevel > xpForCurrentLevel)
                ((state.xp - xpForCurrentLevel).toFloat() / (xpForNextLevel - xpForCurrentLevel)).coerceIn(0f, 1f)
            else 1f
            LinearProgressIndicator(
                progress = { xpProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = OreGold,
                trackColor = CiviltasSurface
            )
            Text("XP: ${state.xp} / $xpForNextLevel", color = TextSecondary, fontSize = 10.sp)

            Spacer(Modifier.height(16.dp))

            // Resources
            Text("RESOURCES", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ResourceCard(Modifier.weight(1f), "⛏", "Ore", state.ore, state.orePerSecond, OreGold)
                ResourceCard(Modifier.weight(1f), "🪨", "Stone", state.stone, state.stonePerSecond, StoneGray)
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ResourceCard(Modifier.weight(1f), "📚", "Knowledge", state.knowledge, state.knowledgePerSecond, KnowledgeBlue)
                ResourceCard(Modifier.weight(1f), "⚡", "Energy", state.energy, 0.0, AccentGreen)
            }

            Spacer(Modifier.height(16.dp))

            // Manual mine button
            Button(
                onClick = { viewModel.collectOre() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OreGold, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("⛏  MINE ORE", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.height(16.dp))

            // Daily Check-in
            if (QuestEngine.isDailyCheckInAvailable(state)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.performDailyCheckIn() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2332)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📅", fontSize = 24.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Daily Check-In", color = AccentGreen, fontWeight = FontWeight.SemiBold)
                            Text("Streak: ${state.dailyCheckInStreak} days  •  Tap to claim!", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            // Catastrophe Forecast
            CatastropheForecastCard(state.catastropheProgress, state.catastropheSeasonDay)

            Spacer(Modifier.height(12.dp))

            // Gnosis Track
            GnosisCard(state.gnosisLevel, state.knowledge)
        }
    }
}

@Composable
private fun ResourceCard(
    modifier: Modifier = Modifier,
    icon: String, name: String,
    amount: Double, perSecond: Double, color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CiviltasSurface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("$icon $name", color = color, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(fmt(amount), color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            if (perSecond > 0) Text("+${fmtRate(perSecond)}/s", color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
private fun CatastropheForecastCard(progress: Float, seasonDay: Int) {
    val label = CatastropheEngine.threatLabel(progress)
    val colorLong = CatastropheEngine.threatColor(progress)
    val color = Color(colorLong.toInt())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CiviltasSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("☄ CATASTROPHE FORECAST", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = color,
                trackColor = CiviltasBorder
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Season day $seasonDay / 7  •  Prepare your civilization",
                color = TextSecondary, fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun GnosisCard(gnosisLevel: Int, knowledge: Double) {
    val nextThreshold = ((gnosisLevel + 1) * 100.0)
    val gnProgress = (knowledge / nextThreshold).coerceIn(0.0, 1.0).toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CiviltasSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, KnowledgeBlue)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("🔮 GNOSIS TRACK", color = KnowledgeBlue, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Text("Rank $gnosisLevel", color = KnowledgeBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { gnProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = KnowledgeBlue,
                trackColor = CiviltasBorder
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Knowledge: ${fmt(knowledge)} / ${fmt(nextThreshold)}  •  Unlock new mechanics at next rank",
                color = TextSecondary, fontSize = 11.sp
            )
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Triple(Screen.Home.route, Icons.Default.Home, "Home"),
        Triple(Screen.Upgrades.route, Icons.Default.Build, "Upgrades"),
        Triple(Screen.Skills.route, Icons.Default.Star, "Skills"),
        Triple(Screen.Quests.route, Icons.Default.List, "Quests"),
        Triple(Screen.Settings.route, Icons.Default.Settings, "Settings"),
    )
    NavigationBar(containerColor = CiviltasSurface) {
        val currentRoute = navController.currentDestination?.route
        items.forEach { (route, icon, label) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OreGold,
                    selectedTextColor = OreGold,
                    indicatorColor = CiviltasDark
                )
            )
        }
    }
}

internal fun fmt(v: Double): String = when {
    v >= 1_000_000 -> String.format(Locale.US, "%.2fM", v / 1_000_000)
    v >= 1_000 -> String.format(Locale.US, "%.2fK", v / 1_000)
    else -> String.format(Locale.US, "%.1f", v)
}

internal fun fmtRate(v: Double): String = String.format(Locale.US, "%.3f", v)

internal fun formatDuration(seconds: Double): String {
    val s = seconds.toLong()
    return when {
        s >= 3600 -> "${s / 3600}h ${(s % 3600) / 60}m"
        s >= 60 -> "${s / 60}m ${s % 60}s"
        else -> "${s}s"
    }
}
