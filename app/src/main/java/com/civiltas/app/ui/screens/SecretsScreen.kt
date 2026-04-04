package com.civiltas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civiltas.app.data.SecretCategory
import com.civiltas.app.data.SecretEntry
import com.civiltas.app.ui.theme.*

@Composable
fun SecretsScreen(viewModel: GameViewModel) {
    val state by viewModel.uiState.collectAsState()
    val secrets = state.secrets
    var selectedCategory by remember { mutableStateOf<SecretCategory?>(null) }

    val filtered = if (selectedCategory == null) secrets
    else secrets.filter { it.category == selectedCategory }

    val unlockedCount = secrets.count { it.isUnlocked }

    Column(modifier = Modifier.fillMaxSize().background(DeepNavy)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                "ORDER OF THE COMPASS", fontSize = 10.sp, color = Gold, letterSpacing = 3.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text("Secrets Library", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(4.dp))
            Text("$unlockedCount / ${secrets.size} Secrets Collected", fontSize = 13.sp, color = TextMuted)
        }

        LinearProgressIndicator(
            progress = { if (secrets.isEmpty()) 0f else unlockedCount.toFloat() / secrets.size },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = Gold,
            trackColor = SlateLight
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Gold,
                        selectedLabelColor = DeepNavy
                    )
                )
            }
            items(SecretCategory.values().toList()) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                    label = { Text(cat.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Gold,
                        selectedLabelColor = DeepNavy
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filtered) { secret ->
                SecretCard(secret = secret)
            }
        }
    }
}

@Composable
fun SecretCard(secret: SecretEntry) {
    val categoryColor = when (secret.category) {
        SecretCategory.LORE -> SkyBlue
        SecretCategory.SURVIVAL_INTEL -> DangerRed
        SecretCategory.RESOURCE_INTEL -> Emerald
        SecretCategory.SACRED_GEOMETRY -> Gold
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = categoryColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = secret.category.displayName,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp, color = categoryColor, fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(secret.earnSource.displayName, fontSize = 10.sp, color = TextMuted)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (secret.isUnlocked) secret.title else "??? ${secret.title.take(3)}...",
                        fontWeight = FontWeight.Bold, fontSize = 16.sp,
                        color = if (secret.isUnlocked) TextPrimary else TextMuted
                    )
                }
                if (!secret.isUnlocked) {
                    Text("🔒", fontSize = 20.sp, modifier = Modifier.alpha(0.6f))
                } else {
                    Text("✓", fontSize = 16.sp, color = Emerald, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(8.dp))

            if (secret.isUnlocked) {
                Text(secret.description, fontSize = 13.sp, color = TextPrimary, lineHeight = 20.sp)
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = Emerald.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        "Effect: ${secret.effect}",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 12.sp, color = Emerald
                    )
                }
            } else {
                Text(
                    "\"${secret.hint}\"",
                    fontSize = 13.sp, color = TextMuted, fontStyle = FontStyle.Italic, lineHeight = 20.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "[LOCKED]", fontSize = 11.sp, color = DangerRed.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp
                )
            }
        }
    }
}
