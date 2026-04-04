package com.sovereign.civiltas.domain.engine

import com.sovereign.civiltas.domain.model.GameState
import com.sovereign.civiltas.domain.model.Quest
import com.sovereign.civiltas.domain.model.DAILY_QUESTS
import com.sovereign.civiltas.domain.model.ROTATING_QUESTS
import java.util.Calendar

object QuestEngine {

    fun getActiveQuests(state: GameState): List<Quest> {
        val dailies = DAILY_QUESTS.map { q ->
            q.copy(isCompleted = q.id in state.completedQuestIds)
        }
        val rotating = ROTATING_QUESTS
            .filterNot { it.id in state.completedQuestIds }
            .take(3)
            .map { q -> q.copy(isCompleted = false) }
        return dailies + rotating
    }

    fun checkQuestCompletion(quest: Quest, state: GameState): Boolean {
        if (quest.isCompleted || quest.id in state.completedQuestIds) return false
        return state.ore >= quest.requireOre &&
               state.stone >= quest.requireStone &&
               state.knowledge >= quest.requireKnowledge
    }

    fun claimQuest(quest: Quest, state: GameState): GameState {
        if (!checkQuestCompletion(quest, state)) return state
        return state.copy(
            ore = state.ore + quest.rewardOre - quest.requireOre,
            stone = state.stone + quest.rewardStone - quest.requireStone,
            knowledge = state.knowledge,
            xp = state.xp + quest.rewardXp,
            skillPoints = state.skillPoints + quest.rewardSkillPoints,
            completedQuestIds = state.completedQuestIds + quest.id
        )
    }

    fun isDailyCheckInAvailable(state: GameState): Boolean {
        val now = Calendar.getInstance()
        val last = Calendar.getInstance().also { it.timeInMillis = state.lastCheckInEpoch }
        return now.get(Calendar.DAY_OF_YEAR) != last.get(Calendar.DAY_OF_YEAR) ||
               now.get(Calendar.YEAR) != last.get(Calendar.YEAR)
    }

    fun performDailyCheckIn(state: GameState, nowEpoch: Long): GameState {
        if (!isDailyCheckInAvailable(state)) return state
        val streak = state.dailyCheckInStreak + 1
        val bonusOre = 50.0 * streak
        val bonusXp = 100L * streak
        return state.copy(
            dailyCheckInStreak = streak,
            lastCheckInEpoch = nowEpoch,
            ore = state.ore + bonusOre,
            xp = state.xp + bonusXp
        )
    }
}
