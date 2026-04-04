package com.civiltas.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.civiltas.app.ui.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTasksScreen(vm: GameViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()
    val completedCount = state.dailyTasks.count { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📋 Daily Tasks ($completedCount/${state.dailyTasks.size})") }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.dailyTasks) { task ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (task.isCompleted) {
                        CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    } else CardDefaults.elevatedCardColors()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                            )
                            Text(
                                text = task.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "+${task.xpReward} XP",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        if (!task.isCompleted) {
                            Button(
                                onClick = { vm.completeTask(task.id, task.xpReward) },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text("Claim", style = MaterialTheme.typography.labelMedium)
                            }
                        } else {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.CheckCircle,
                                contentDescription = "Done",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}
