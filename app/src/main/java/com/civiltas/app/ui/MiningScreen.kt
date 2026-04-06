package com.civiltas.app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.civiltas.app.MiningUiState
import com.civiltas.app.MiningViewModel
import com.civiltas.app.R
import kotlin.math.cos
import kotlin.math.sin

// ── Colour palette (dark navy / gold / teal) ──────────────────────────────────
private val NavyDeep = Color(0xFF020617)
private val NavyCard = Color(0xFF1E293B)
private val NavyBorder = Color(0xFF334155)
private val GoldBright = Color(0xFFFACC15)
private val GoldDim = Color(0xFFCA8A04)
private val TealAccent = Color(0xFF38BDF8)
private val TealDim = Color(0xFF0E7490)
private val SilverText = Color(0xFFCBD5E1)
private val GreenAccent = Color(0xFF10B981)

// ── Entry point ───────────────────────────────────────────────────────────────

@Composable
fun MiningScreen(
    modifier: Modifier = Modifier,
    viewModel: MiningViewModel = viewModel(
        factory = MiningViewModel.Factory(LocalContext.current)
    )
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MiningScreenContent(
        modifier = modifier,
        state = state,
        onTapMine = viewModel::onTapMine,
        onCollect = viewModel::onCollectPassive,
        onUpgrade = viewModel::onUpgradeTap,
    )
}

// ── Pure-state composable (testable / previewable) ────────────────────────────

@Composable
fun MiningScreenContent(
    state: MiningUiState,
    onTapMine: () -> Unit,
    onCollect: () -> Unit,
    onUpgrade: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = NavyDeep,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Header ──────────────────────────────────────────────────────
            Text(
                text = stringResource(R.string.label_order_of_compass),
                color = GoldBright,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "CIVILTAS",
                color = SilverText,
                fontSize = 12.sp,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Compass rose ────────────────────────────────────────────────
            CompassRose(isMining = state.isMining)

            Spacer(modifier = Modifier.height(20.dp))

            // ── Stats card ──────────────────────────────────────────────────
            StatsCard(state = state)

            Spacer(modifier = Modifier.height(16.dp))

            // ── Passive progress card ───────────────────────────────────────
            PassiveProgressCard(
                progress = state.passiveProgress,
                pendingOre = state.pendingPassiveOre,
                passiveRate = state.passiveOrePerHour,
                onCollect = onCollect,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Primary MINE button ─────────────────────────────────────────
            MineButton(isMining = state.isMining, onTap = onTapMine)

            Spacer(modifier = Modifier.height(12.dp))

            // ── Session taps readout ────────────────────────────────────────
            Text(
                text = stringResource(R.string.label_session_taps, formatNumber(state.sessionTaps.toLong())),
                color = SilverText,
                fontSize = 12.sp,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Upgrade button ──────────────────────────────────────────────
            UpgradeButton(
                orePerTap = state.orePerTap,
                upgradeCost = state.tapUpgradeCost,
                totalOre = state.totalOre,
                onUpgrade = onUpgrade,
            )
        }
    }
}

// ── Compass Rose ──────────────────────────────────────────────────────────────

/**
 * A hand-drawn compass/astrolabe inspired by "Order of the Compass" theme.
 * - Slow continuous rotation when idle.
 * - Fast rotation + bright glow when [isMining] is true.
 */
@Composable
fun CompassRose(
    isMining: Boolean,
    modifier: Modifier = Modifier,
) {
    val infinite = rememberInfiniteTransition(label = "compass_idle")
    val idleRotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12_000, easing = LinearEasing)
        ),
        label = "idle_rotation",
    )

    // Fast spin overlay driven by isMining.
    val fastRotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing)
        ),
        label = "fast_rotation",
    )

    val rotationDegrees = if (isMining) fastRotation else idleRotation

    val outerRingAlpha by animateFloatAsState(
        targetValue = if (isMining) 1f else 0.6f,
        animationSpec = tween(300),
        label = "outer_alpha",
    )

    val innerGlowRadius by animateFloatAsState(
        targetValue = if (isMining) 60f else 30f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "glow_radius",
    )

    val compassCd = if (isMining) {
        stringResource(R.string.cd_compass_spinning)
    } else {
        stringResource(R.string.cd_compass_idle)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(200.dp)
            .semantics { contentDescription = compassCd },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val outerRadius = size.minDimension / 2f

            // Glow halo.
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GoldBright.copy(alpha = 0.25f * outerRingAlpha),
                        Color.Transparent,
                    ),
                    center = Offset(cx, cy),
                    radius = innerGlowRadius,
                ),
                radius = innerGlowRadius,
                center = Offset(cx, cy),
            )

            // Outer decorative ring.
            drawCircle(
                color = NavyBorder.copy(alpha = outerRingAlpha),
                radius = outerRadius - 2f,
                center = Offset(cx, cy),
                style = Stroke(width = 3f),
            )

            // Second ring.
            drawCircle(
                color = GoldDim.copy(alpha = outerRingAlpha),
                radius = outerRadius * 0.85f,
                center = Offset(cx, cy),
                style = Stroke(width = 1.5f),
            )

            // Rotating compass elements.
            rotate(degrees = rotationDegrees, pivot = Offset(cx, cy)) {
                drawCompassPoints(cx, cy, outerRadius, outerRingAlpha)
                drawCompassNeedle(cx, cy, outerRadius, outerRingAlpha)
            }

            // Static hub.
            drawCircle(
                color = GoldBright,
                radius = 8f,
                center = Offset(cx, cy),
            )
            drawCircle(
                color = NavyDeep,
                radius = 4f,
                center = Offset(cx, cy),
            )
        }
    }
}

/** Draws the 8-point compass rose and tick marks. */
private fun DrawScope.drawCompassPoints(cx: Float, cy: Float, radius: Float, alpha: Float) {
    val cardinal = listOf(0f, 90f, 180f, 270f)
    val intercardinal = listOf(45f, 135f, 225f, 315f)

    // Cardinal (N/S/E/W) points — longer gold diamonds.
    cardinal.forEach { angleDeg ->
        val angleRad = Math.toRadians(angleDeg.toDouble() - 90.0)
        val tipX = cx + (radius * 0.75f * cos(angleRad)).toFloat()
        val tipY = cy + (radius * 0.75f * sin(angleRad)).toFloat()
        val baseX = cx + (radius * 0.15f * cos(angleRad + Math.PI / 2)).toFloat()
        val baseY = cy + (radius * 0.15f * sin(angleRad + Math.PI / 2)).toFloat()
        val base2X = cx + (radius * 0.15f * cos(angleRad - Math.PI / 2)).toFloat()
        val base2Y = cy + (radius * 0.15f * sin(angleRad - Math.PI / 2)).toFloat()
        val rootX = cx + (radius * 0.25f * cos(angleRad + Math.PI)).toFloat()
        val rootY = cy + (radius * 0.25f * sin(angleRad + Math.PI)).toFloat()

        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(tipX, tipY)
            lineTo(baseX, baseY)
            lineTo(rootX, rootY)
            lineTo(base2X, base2Y)
            close()
        }
        drawPath(path = path, color = GoldBright.copy(alpha = alpha))
    }

    // Inter-cardinal points — shorter teal diamonds.
    intercardinal.forEach { angleDeg ->
        val angleRad = Math.toRadians(angleDeg.toDouble() - 90.0)
        val tipX = cx + (radius * 0.55f * cos(angleRad)).toFloat()
        val tipY = cy + (radius * 0.55f * sin(angleRad)).toFloat()
        val baseX = cx + (radius * 0.10f * cos(angleRad + Math.PI / 2)).toFloat()
        val baseY = cy + (radius * 0.10f * sin(angleRad + Math.PI / 2)).toFloat()
        val base2X = cx + (radius * 0.10f * cos(angleRad - Math.PI / 2)).toFloat()
        val base2Y = cy + (radius * 0.10f * sin(angleRad - Math.PI / 2)).toFloat()
        val rootX = cx + (radius * 0.15f * cos(angleRad + Math.PI)).toFloat()
        val rootY = cy + (radius * 0.15f * sin(angleRad + Math.PI)).toFloat()

        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(tipX, tipY)
            lineTo(baseX, baseY)
            lineTo(rootX, rootY)
            lineTo(base2X, base2Y)
            close()
        }
        drawPath(path = path, color = TealAccent.copy(alpha = alpha * 0.85f))
    }

    // Tick marks on outer ring.
    for (i in 0 until 24) {
        val angleDeg = i * 15f
        val angleRad = Math.toRadians(angleDeg.toDouble() - 90.0)
        val isMajor = i % 3 == 0
        val startR = if (isMajor) radius * 0.88f else radius * 0.92f
        val endR = radius * 0.97f
        drawLine(
            color = (if (isMajor) GoldDim else NavyBorder).copy(alpha = alpha),
            start = Offset(cx + (startR * cos(angleRad)).toFloat(), cy + (startR * sin(angleRad)).toFloat()),
            end = Offset(cx + (endR * cos(angleRad)).toFloat(), cy + (endR * sin(angleRad)).toFloat()),
            strokeWidth = if (isMajor) 2.5f else 1f,
            cap = StrokeCap.Round,
        )
    }
}

/** Draws a compass needle with a gold/teal bicolour. */
private fun DrawScope.drawCompassNeedle(cx: Float, cy: Float, radius: Float, alpha: Float) {
    // North half — gold.
    drawLine(
        color = GoldBright.copy(alpha = alpha),
        start = Offset(cx, cy),
        end = Offset(cx, cy - radius * 0.6f),
        strokeWidth = 4f,
        cap = StrokeCap.Round,
    )
    // South half — teal.
    drawLine(
        color = TealAccent.copy(alpha = alpha),
        start = Offset(cx, cy),
        end = Offset(cx, cy + radius * 0.4f),
        strokeWidth = 4f,
        cap = StrokeCap.Round,
    )
}

// ── Stats card ────────────────────────────────────────────────────────────────

@Composable
fun StatsCard(state: MiningUiState) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                StatItem(
                    label = stringResource(R.string.label_total_ore),
                    value = formatNumber(state.totalOre),
                    valueColor = GoldBright,
                )
                StatItem(
                    label = stringResource(R.string.label_tap_rate, state.orePerTap),
                    value = "${state.orePerTap} ore/tap",
                    valueColor = TealAccent,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.label_passive_rate, formatNumber(state.passiveOrePerHour)),
                color = SilverText,
                fontSize = 11.sp,
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = label, color = SilverText, fontSize = 10.sp)
        Text(
            text = value,
            color = valueColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

// ── Passive progress card ─────────────────────────────────────────────────────

@Composable
fun PassiveProgressCard(
    progress: Float,
    pendingOre: Long,
    passiveRate: Long,
    onCollect: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_pending_ore),
                        color = SilverText,
                        fontSize = 11.sp,
                    )
                    Text(
                        text = formatNumber(pendingOre),
                        color = GreenAccent,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                OutlinedButton(
                    onClick = onCollect,
                    enabled = pendingOre > 0,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenAccent),
                    modifier = Modifier.semantics {
                        contentDescription = "Collect passive ore"
                    },
                ) {
                    Text(text = stringResource(R.string.btn_collect))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(durationMillis = 500),
                label = "passive_progress",
            )
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .semantics {
                        contentDescription =
                            "Passive collection progress: ${(progress * 100).toInt()} percent"
                    },
                color = GreenAccent,
                trackColor = NavyBorder,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.label_passive_progress, (progress * 100).toInt()),
                color = SilverText,
                fontSize = 10.sp,
            )
        }
    }
}

// ── Mine button ───────────────────────────────────────────────────────────────

@Composable
fun MineButton(isMining: Boolean, onTap: () -> Unit) {
    var pressCount by remember { mutableIntStateOf(0) }

    val scale by animateFloatAsState(
        targetValue = if (isMining) 0.93f else 1f,
        animationSpec = tween(durationMillis = 120, easing = FastOutSlowInEasing),
        label = "mine_button_scale",
    )

    val glowColor by animateColorAsState(
        targetValue = if (isMining) GoldBright else GoldDim,
        animationSpec = tween(200),
        label = "mine_button_glow",
    )

    val buttonCd = stringResource(R.string.cd_mine_button)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(140.dp)
            .scale(scale)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.3f),
                        Color.Transparent,
                    ),
                ),
                shape = CircleShape,
            ),
    ) {
        Button(
            onClick = {
                pressCount++
                onTap()
            },
            modifier = Modifier
                .size(120.dp)
                .semantics { contentDescription = buttonCd },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isMining) GoldBright else GoldDim,
                contentColor = NavyDeep,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 2.dp,
            ),
        ) {
            Text(
                text = stringResource(R.string.btn_mine),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
            )
        }
    }
}

// ── Upgrade button ────────────────────────────────────────────────────────────

@Composable
fun UpgradeButton(
    orePerTap: Long,
    upgradeCost: Long,
    totalOre: Long,
    onUpgrade: () -> Unit,
) {
    val canAfford = totalOre >= upgradeCost
    Button(
        onClick = onUpgrade,
        enabled = canAfford,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = TealDim,
            contentColor = Color.White,
            disabledContainerColor = NavyBorder,
            disabledContentColor = SilverText,
        ),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.btn_upgrade, orePerTap + 1),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.label_upgrade_cost, formatNumber(upgradeCost)),
                fontSize = 10.sp,
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

/** Human-readable number with K / M suffix. */
fun formatNumber(n: Long): String = when {
    n >= 1_000_000L -> "%.1fM".format(n / 1_000_000.0)
    n >= 1_000L -> "%.1fK".format(n / 1_000.0)
    else -> n.toString()
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF020617)
@Composable
private fun MiningScreenPreview() {
    MiningScreenContent(
        state = MiningUiState(
            totalOre = 1234L,
            pendingPassiveOre = 5L,
            orePerTap = 1L,
            passiveOrePerHour = 120L,
            isMining = false,
            sessionTaps = 42,
            tapUpgradeCost = 50L,
            passiveProgress = 0.65f,
        ),
        onTapMine = {},
        onCollect = {},
        onUpgrade = {},
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF020617)
@Composable
private fun MiningScreenMiningPreview() {
    MiningScreenContent(
        state = MiningUiState(
            totalOre = 9_876L,
            pendingPassiveOre = 30L,
            orePerTap = 3L,
            passiveOrePerHour = 240L,
            isMining = true,
            sessionTaps = 150,
            tapUpgradeCost = 450L,
            passiveProgress = 0.3f,
        ),
        onTapMine = {},
        onCollect = {},
        onUpgrade = {},
    )
}
