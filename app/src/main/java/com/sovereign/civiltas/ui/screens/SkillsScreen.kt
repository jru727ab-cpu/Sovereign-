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
import com.sovereign.civiltas.domain.model.ALL_SKILLS
import com.sovereign.civiltas.domain.model.Skill
import com.sovereign.civiltas.domain.model.SkillTree
import com.sovereign.civiltas.ui.theme.*
import com.sovereign.civiltas.ui.viewmodel.GameViewModel

@Composable
fun SkillsScreen(viewModel: GameViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()

    Scaffold(bottomBar = { BottomNavBar(navController) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CiviltasDark)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("SKILLS", color = OreGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("${state.skillPoints} SP available", color = AccentGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Text("Unlock perks that shape your playstyle", color = TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))

            SkillTree.entries.forEach { tree ->
                Text(
                    tree.name,
                    color = when (tree) {
                        SkillTree.MINER -> OreGold
                        SkillTree.BUILDER -> StoneGray
                        SkillTree.SCHOLAR -> KnowledgeBlue
                    },
                    fontWeight = FontWeight.SemiBold, fontSize = 15.sp
                )
                Spacer(Modifier.height(8.dp))
                ALL_SKILLS.filter { it.tree == tree }.forEach { skill ->
                    val isUnlocked = skill.id in state.unlockedSkillIds
                    val prereqMet = skill.requires == null || skill.requires in state.unlockedSkillIds
                    val canAfford = state.skillPoints >= skill.cost
                    SkillCard(
                        skill = skill,
                        isUnlocked = isUnlocked,
                        prereqMet = prereqMet,
                        canAfford = canAfford,
                        onUnlock = { viewModel.unlockSkill(skill.id) }
                    )
                    Spacer(Modifier.height(8.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SkillCard(
    skill: Skill,
    isUnlocked: Boolean,
    prereqMet: Boolean,
    canAfford: Boolean,
    onUnlock: () -> Unit
) {
    val containerColor = when {
        isUnlocked -> Color(0xFF1C3A1C)
        !prereqMet -> Color(0xFF1A1A1A)
        else -> CiviltasSurface
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    skill.name,
                    color = if (isUnlocked) AccentGreen else if (!prereqMet) TextSecondary else TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(skill.description, color = TextSecondary, fontSize = 12.sp)
                if (skill.requires != null && !prereqMet) {
                    Text("Requires: ${skill.requires}", color = CatastropheRed, fontSize = 11.sp)
                }
            }
            Spacer(Modifier.width(8.dp))
            if (isUnlocked) {
                Text("✓ Active", color = AccentGreen, fontWeight = FontWeight.Bold)
            } else {
                Button(
                    onClick = onUnlock,
                    enabled = prereqMet && canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OreGold, contentColor = Color.Black,
                        disabledContainerColor = CiviltasBorder
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("${skill.cost} SP")
                }
            }
        }
    }
}
