package com.civiltas.app.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civiltas.app.data.ForecastRepository
import com.civiltas.app.data.SecretsCatalog
import com.civiltas.app.data.SecretsRepository
import com.civiltas.app.data.model.Secret
import com.civiltas.app.data.model.SecretEffect
import com.civiltas.app.ui.theme.*

/**
 * Secret Detail screen — shows full lore, effect, and unlock/purchase CTA.
 *
 * If the secret is locked, lore is hidden and replaced with a prompt to earn or purchase.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecretDetailScreen(
    secretId: String,
    secretsRepository: SecretsRepository,
    forecastRepository: ForecastRepository,
    onBack: () -> Unit,
    onOpenStore: () -> Unit
) {
    val secret = SecretsCatalog.findById(secretId) ?: run {
        onBack()
        return
    }

    var isUnlocked by remember { mutableStateOf(secretsRepository.isUnlocked(secretId)) }
    val confidence = remember { forecastRepository.getConfidence() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isUnlocked) secret.title else "Locked Secret",
                        color = if (isUnlocked) Gold else TextDim,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyCard)
            )
        },
        containerColor = NavyDeep
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Header card ────────────────────────────────────────────────
            Surface(
                color = NavyCard,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (isUnlocked) tierColor(secret.tier) else Locked, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isUnlocked) Icons.Default.Star else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isUnlocked) tierColor(secret.tier) else TextDim,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                CategoryBadge(category = secret.category)
                                TierBadge(tier = secret.tier)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = secret.category.pillar,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = if (isUnlocked) secret.title else "████████ ███████ ████",
                        color = if (isUnlocked) TextPrimary else TextDim,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            // ── Lore ───────────────────────────────────────────────────────
            Surface(
                color = NavyCard,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ARCHIVE ENTRY",
                        color = SlateBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isUnlocked) {
                        Text(
                            text = secret.lore,
                            color = TextSecondary,
                            lineHeight = 22.sp
                        )
                    } else {
                        Text(
                            text = "████████ ████████ ██████ ████ ██████████ ████ " +
                                "████ ██████ ████████ ████ ██████ ████████ ██████.",
                            color = Locked,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // ── Effect ─────────────────────────────────────────────────────
            if (isUnlocked) {
                Surface(
                    color = NavyCard,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "EFFECT",
                            color = SlateBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = secret.effectDescription,
                            color = Gold,
                            fontWeight = FontWeight.SemiBold
                        )
                        // Extra forecast context for confidence-affecting secrets
                        if (secret.effect is SecretEffect.ForecastConfidence) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Current Forecast Confidence: $confidence / 100",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // ── CTA: Unlock or re-confirmation ────────────────────────────
            if (!isUnlocked) {
                LockedSecretCta(
                    secret = secret,
                    onOpenStore = onOpenStore
                )
            } else {
                Surface(
                    color = Success.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✦ This secret has been added to your archive.",
                        color = Success,
                        modifier = Modifier.padding(14.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LockedSecretCta(
    secret: Secret,
    onOpenStore: () -> Unit
) {
    Surface(
        color = NavyCard,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Amber, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "OBTAIN THIS SECRET",
                color = Amber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Earn path
            Surface(
                color = NavySurface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Earn via Gameplay",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = secret.unlockSource.displayName,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Success,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Purchase path (if available)
            if (secret.storeSkuId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onOpenStore,
                    colors = ButtonDefaults.buttonColors(containerColor = Gold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Purchase (Accelerate)",
                        color = NavyDeep,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Purchases accelerate access and unlock extra lore. " +
                        "All meaningful progression is earnable for free.",
                    color = TextDim,
                    fontSize = 11.sp
                )
            }
        }
    }
}
