package com.civiltas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civiltas.app.data.EarningEngine
import com.civiltas.app.data.ForecastRepository
import com.civiltas.app.data.SecretsCatalog
import com.civiltas.app.data.SecretsRepository
import com.civiltas.app.data.model.Secret
import com.civiltas.app.data.model.SecretCategory
import com.civiltas.app.data.model.SecretTier
import com.civiltas.app.ui.theme.*

/**
 * Secrets Library screen — the main secrets browsing experience.
 *
 * Displays:
 * - Forecast Confidence meter at the top
 * - Category filter tabs
 * - List of secrets (unlocked showing detail, locked showing silhouette)
 * - CTA buttons: earn (via milestone) or purchase (store)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecretsLibraryScreen(
    secretsRepository: SecretsRepository,
    forecastRepository: ForecastRepository,
    earningEngine: EarningEngine,
    onSecretClick: (String) -> Unit,
    onOpenStore: () -> Unit
) {
    var unlockedIds by remember { mutableStateOf(secretsRepository.unlockedIds()) }
    var confidence by remember { mutableIntStateOf(forecastRepository.getConfidence()) }
    var arrivalWindow by remember { mutableStateOf(forecastRepository.getArrivalWindow()) }
    var selectedCategory by remember { mutableStateOf<SecretCategory?>(null) }
    var lastUnlocked by remember { mutableStateOf<String?>(null) }

    fun refresh() {
        unlockedIds = secretsRepository.unlockedIds()
        confidence = forecastRepository.getConfidence()
        arrivalWindow = forecastRepository.getArrivalWindow()
    }

    val displayedSecrets = remember(selectedCategory) {
        if (selectedCategory == null) SecretsCatalog.all
        else SecretsCatalog.all.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Secrets Library",
                        color = Gold,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavyCard
                ),
                actions = {
                    TextButton(onClick = onOpenStore) {
                        Text("Store", color = SlateBlue)
                    }
                }
            )
        },
        containerColor = NavyDeep
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ── Forecast Meter ─────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(12.dp))
                ForecastMeterCard(confidence = confidence, arrivalWindow = arrivalWindow)
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Earn via Milestone shortcut ────────────────────────────────
            item {
                EarnMilestoneCard(
                    taskStreak = earningEngine.getDailyTaskStreak(),
                    expeditionCount = earningEngine.getExpeditionCount(),
                    onCompleteTask = {
                        lastUnlocked = earningEngine.recordDailyTask()
                        refresh()
                    },
                    onCompleteExpedition = {
                        lastUnlocked = earningEngine.recordExpedition()
                        refresh()
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // ── Unlock notification ────────────────────────────────────────
            lastUnlocked?.let { id ->
                item {
                    val secret = SecretsCatalog.findById(id)
                    if (secret != null) {
                        Surface(
                            color = Success.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✦ New Secret Unlocked: ${secret.title}",
                                color = Success,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // ── Category filters ───────────────────────────────────────────
            item {
                CategoryFilterRow(
                    selected = selectedCategory,
                    onSelect = { selectedCategory = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── Secrets list ───────────────────────────────────────────────
            items(displayedSecrets, key = { it.id }) { secret ->
                SecretListItem(
                    secret = secret,
                    isUnlocked = secret.id in unlockedIds,
                    onClick = { onSecretClick(secret.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// ── Sub-composables ────────────────────────────────────────────────────────────

@Composable
fun ForecastMeterCard(confidence: Int, arrivalWindow: String) {
    Surface(
        color = NavyCard,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Forecast Confidence",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$confidence / 100",
                    color = Gold,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { confidence / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Gold,
                trackColor = NavySurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Estimated arrival window: $arrivalWindow",
                color = TextSecondary,
                fontSize = 13.sp
            )
            if (confidence < 30) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Unlock more Secrets to narrow the window.",
                    color = Amber,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun EarnMilestoneCard(
    taskStreak: Int,
    expeditionCount: Int,
    onCompleteTask: () -> Unit,
    onCompleteExpedition: () -> Unit
) {
    Surface(
        color = NavyCard,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Earn Secrets",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Daily Task Streak: $taskStreak",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Next unlock at 7, 14, 21...",
                        color = TextDim,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = onCompleteTask,
                        colors = ButtonDefaults.buttonColors(containerColor = SlateBlue),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Text("Complete Task", fontSize = 12.sp, color = NavyDeep)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Expeditions: $expeditionCount",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Next unlock at 3, 10, 25...",
                        color = TextDim,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = onCompleteExpedition,
                        colors = ButtonDefaults.buttonColors(containerColor = Amber),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Text("Expedition", fontSize = 12.sp, color = NavyDeep)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterRow(
    selected: SecretCategory?,
    onSelect: (SecretCategory?) -> Unit
) {
    val categories = listOf(null) + SecretCategory.entries
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEach { cat ->
            val isSelected = cat == selected
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(cat) },
                label = {
                    Text(
                        text = cat?.displayName ?: "All",
                        fontSize = 11.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Gold,
                    selectedLabelColor = NavyDeep,
                    containerColor = NavySurface,
                    labelColor = TextSecondary
                )
            )
        }
    }
}

@Composable
fun SecretListItem(
    secret: Secret,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isUnlocked && secret.tier == SecretTier.CLASSIFIED -> Gold
        isUnlocked && secret.tier == SecretTier.RARE -> Amber
        isUnlocked -> NavyBorder
        else -> Locked
    }

    Surface(
        color = if (isUnlocked) NavyCard else NavyCard.copy(alpha = 0.7f),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lock / tier indicator
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isUnlocked) tierColor(secret.tier).copy(alpha = 0.15f) else Locked.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = tierColor(secret.tier),
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = TextDim,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isUnlocked) secret.title else "████████ ███████",
                    color = if (isUnlocked) TextPrimary else TextDim,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CategoryBadge(category = secret.category)
                    TierBadge(tier = secret.tier)
                }
                if (isUnlocked) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = secret.effectDescription,
                        color = SlateBlue,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBadge(category: SecretCategory) {
    val color = when (category) {
        SecretCategory.DIRECTORATE_INTEL, SecretCategory.CONTINUITY_PROTOCOL -> SlateBlue
        SecretCategory.ORDER_GEOMETRY -> Amber
        SecretCategory.SURVIVOR_TESTIMONY -> Success
    }
    Text(
        text = category.displayName,
        color = color,
        fontSize = 10.sp,
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
fun TierBadge(tier: SecretTier) {
    val color = tierColor(tier)
    Text(
        text = tier.displayName,
        color = color,
        fontSize = 10.sp,
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

fun tierColor(tier: SecretTier): Color = when (tier) {
    SecretTier.COMMON -> Color(0xFF94A3B8)
    SecretTier.UNCOMMON -> Color(0xFF34D399)
    SecretTier.RARE -> Color(0xFF818CF8)
    SecretTier.CLASSIFIED -> Gold
}
