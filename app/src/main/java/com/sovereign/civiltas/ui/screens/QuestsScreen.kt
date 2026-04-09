package com.sovereign.civiltas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sovereign.civiltas.domain.engine.QuestEngine
import com.sovereign.civiltas.domain.model.Quest
import com.sovereign.civiltas.domain.model.QuestType
import com.sovereign.civiltas.ui.theme.*
import com.sovereign.civiltas.ui.viewmodel.GameViewModel

@Composable
fun QuestsScreen(viewModel: GameViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val quests = QuestEngine.getActiveQuests(state)

    Scaffold(bottomBar = { BottomNavBar(navController) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CiviltasDark)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("QUESTS", color = OreGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Complete goals to earn rewards", color = TextSecondary, fontSize = 13.sp)

            Spacer(Modifier.height(16.dp))

            Text("DAILY", color = AccentGreen, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            quests.filter { it.type == QuestType.DAILY }.forEach { quest ->
                QuestCard(
                    quest = quest,
                    canClaim = QuestEngine.checkQuestCompletion(quest, state),
                    onClaim = { viewModel.claimQuest(quest) }
                )
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(12.dp))
            Text("ROTATING", color = KnowledgeBlue, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            quests.filter { it.type == QuestType.ROTATING }.forEach { quest ->
                QuestCard(
                    quest = quest,
                    canClaim = QuestEngine.checkQuestCompletion(quest, state),
                    onClaim = { viewModel.claimQuest(quest) }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun QuestCard(quest: Quest, canClaim: Boolean, onClaim: () -> Unit) {
    val bg = when {
        quest.isCompleted -> Color(0xFF1A2A1A)
        canClaim -> Color(0xFF1A2332)
        else -> CiviltasSurface
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = bg),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    quest.title,
                    color = if (quest.isCompleted) AccentGreen else TextPrimary,
                    fontWeight = FontWeight.SemiBold, fontSize = 15.sp
                )
                if (quest.isCompleted) Text("✓", color = AccentGreen, fontWeight = FontWeight.Bold)
            }
            Text(quest.description, color = TextSecondary, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    if (quest.rewardXp > 0) Text("+${quest.rewardXp} XP", color = OreGold, fontSize = 12.sp)
                    if (quest.rewardOre > 0) Text("+${quest.rewardOre} Ore", color = OreGold, fontSize = 12.sp)
                    if (quest.rewardSkillPoints > 0) Text("+${quest.rewardSkillPoints} SP", color = AccentGreen, fontSize = 12.sp)
                }
                if (!quest.isCompleted) {
                    Button(
                        onClick = onClaim,
                        enabled = canClaim,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen, contentColor = Color.Black,
                            disabledContainerColor = CiviltasBorder
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (canClaim) "Claim!" else "Incomplete", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
