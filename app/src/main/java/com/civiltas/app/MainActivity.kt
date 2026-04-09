package com.civiltas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CiviltasTheme {
                val state by viewModel.state.collectAsState()
                GameScreen(state)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveNow()
    }
}

// ── Theme ─────────────────────────────────────────────────────────────────────

private val DarkBackground = Color(0xFF020617)
private val CardBackground = Color(0xFF1E293B)
private val GoldColor = Color(0xFFFACC15)
private val BlueAccent = Color(0xFF38BDF8)
private val RedAlert = Color(0xFFEF4444)

@Composable
fun CiviltasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = DarkBackground,
            surface = CardBackground,
            primary = GoldColor,
            secondary = BlueAccent
        ),
        content = content
    )
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun GameScreen(state: GameState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Text(
            text = "CIVILTAS",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = GoldColor
        )
        Text(
            text = "Level ${state.level}  •  XP ${state.xp} / ${state.nextLevelXp}",
            fontSize = 14.sp,
            color = BlueAccent
        )

        LinearProgressIndicator(
            progress = { state.xp.toFloat() / state.nextLevelXp.toFloat() },
            modifier = Modifier.fillMaxWidth(),
            color = GoldColor,
            trackColor = CardBackground
        )

        // Resources card
        ResourceCard(state)

        // Catastrophe countdown
        CatastropheCard(state.catastropheCountdownSec)

        // Secret society teaser (unlocks at level 5)
        if (state.level >= 5) {
            SecretSocietyCard()
        }
    }
}

@Composable
fun ResourceCard(state: GameState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("⛏  Resources", fontWeight = FontWeight.SemiBold, color = Color.White)
            ResourceRow(label = "Iron", value = state.iron, rate = Resource.IRON.ratePerSecond)
            ResourceRow(label = "Gold", value = state.gold, rate = Resource.GOLD.ratePerSecond)
            ResourceRow(label = "Oil",  value = state.oil,  rate = Resource.OIL.ratePerSecond)
        }
    }
}

@Composable
fun ResourceRow(label: String, value: Float, rate: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.LightGray, fontSize = 14.sp)
        Text(
            "${value.roundToInt()}  (+${rate}/s)",
            color = GoldColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CatastropheCard(countdownSec: Long) {
    val hours = countdownSec / 3600
    val minutes = (countdownSec % 3600) / 60
    val isUrgent = hours < 24

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUrgent) Color(0xFF7F1D1D) else CardBackground
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "⚠  Catastrophe Countdown",
                fontWeight = FontWeight.SemiBold,
                color = if (isUrgent) RedAlert else Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${hours}h ${minutes}m remaining",
                color = if (isUrgent) RedAlert else Color.LightGray,
                fontSize = 14.sp
            )
            Text(
                "Hoard resources or build for now?",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun SecretSocietyCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A0A2E)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🔒  The Veil", fontWeight = FontWeight.SemiBold, color = Color(0xFF9333EA))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "A message has been left for you…",
                color = Color(0xFF7C3AED),
                fontSize = 13.sp
            )
            Text(
                "(Secret Society — Phase 2 content)",
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}
