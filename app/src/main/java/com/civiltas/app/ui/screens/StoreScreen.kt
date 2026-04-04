package com.civiltas.app.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civiltas.app.data.ForecastRepository
import com.civiltas.app.data.SecretsCatalog
import com.civiltas.app.data.SecretsRepository
import com.civiltas.app.data.StoreRepository
import com.civiltas.app.data.model.SkuType
import com.civiltas.app.data.model.StoreItem
import com.civiltas.app.ui.theme.*

/**
 * Store screen — lists Secrets packs and the Season Pass.
 *
 * Payment processing is **stubbed**. Tapping "Purchase" shows an informational message.
 * Wire [StoreRepository.initiatePurchase] to a real billing provider when ready.
 *
 * Guardrail note: the store description on each item reminds users that all meaningful
 * progression is earnable for free — purchases accelerate access and add story depth.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    secretsRepository: SecretsRepository,
    forecastRepository: ForecastRepository,
    onBack: () -> Unit
) {
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Continuity Store",
                        color = Gold,
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = NavyDeep
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "ARCHIVES AVAILABLE FOR ACQUISITION",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "All meaningful progression is earnable through gameplay. " +
                        "Purchases provide faster access, extra lore, and convenience.",
                    color = TextDim,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(StoreRepository.catalogue, key = { it.skuId }) { item ->
                StoreItemCard(
                    item = item,
                    secretsRepository = secretsRepository,
                    onPurchase = {
                        StoreRepository.initiatePurchase(
                            skuId = item.skuId,
                            onSuccess = { _ ->
                                item.secretIds.forEach { id -> secretsRepository.unlock(id) }
                                forecastRepository.recalculate()
                                snackbarMessage = "Purchase successful! Secrets unlocked."
                            },
                            onError = { msg ->
                                snackbarMessage = msg
                            }
                        )
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun StoreItemCard(
    item: StoreItem,
    secretsRepository: SecretsRepository,
    onPurchase: () -> Unit
) {
    val ownedCount = item.secretIds.count { secretsRepository.isUnlocked(it) }
    val totalCount = item.secretIds.size
    val isFullyOwned = ownedCount == totalCount

    val accentColor = when (item.skuType) {
        SkuType.SEASON_PASS -> Gold
        SkuType.SUBSCRIPTION -> Amber
        SkuType.ONE_TIME_PACK -> SlateBlue
    }

    Surface(
        color = NavyCard,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isFullyOwned) Success else accentColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.displayName,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = skuTypeLabel(item.skuType),
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp
                    )
                }
                Text(
                    text = "$${item.priceUsd}",
                    color = Gold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Contains $totalCount secret${if (totalCount != 1) "s" else ""}  •  Owned: $ownedCount / $totalCount",
                color = TextDim,
                fontSize = 11.sp
            )

            // List unlockable secrets by title
            val unownedSecretTitles = item.secretIds
                .filter { !secretsRepository.isUnlocked(it) }
                .mapNotNull { id ->
                    SecretsCatalog.findById(id)?.let { "• ${it.tier.displayName}: ${it.title}" }
                }
            if (unownedSecretTitles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                unownedSecretTitles.forEach { title ->
                    Text(
                        text = title,
                        color = TextDim,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onPurchase,
                enabled = !isFullyOwned,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    disabledContainerColor = Success.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isFullyOwned) "✦ All Secrets Owned" else "Purchase  $${ item.priceUsd}",
                    color = if (isFullyOwned) Success else NavyDeep,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun skuTypeLabel(skuType: SkuType) = when (skuType) {
    SkuType.ONE_TIME_PACK -> "ONE-TIME PACK"
    SkuType.SEASON_PASS -> "SEASON PASS"
    SkuType.SUBSCRIPTION -> "SUBSCRIPTION"
}
