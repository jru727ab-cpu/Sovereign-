package com.sovereign.wallet

import com.sovereign.wallet.ai.AIAgent
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for the offline AI agent.
 */
class AIAgentTest {

    @Test
    fun `getResponse returns non-empty string`() {
        val response = AIAgent.getResponse("hello")
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `getResponse handles empty input`() {
        val response = AIAgent.getResponse("")
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `getResponse matches help keyword`() {
        val response = AIAgent.getResponse("help me")
        assertTrue(response.contains("help", ignoreCase = true) || response.contains("assist", ignoreCase = true))
    }

    @Test
    fun `getResponse matches clean keyword`() {
        val response = AIAgent.getResponse("how do I clean cache?")
        assertTrue(response.contains("Bug Cleaner", ignoreCase = true) || response.contains("clean", ignoreCase = true))
    }

    @Test
    fun `getResponse matches security keyword`() {
        val response = AIAgent.getResponse("is my data secure?")
        assertTrue(response.isNotEmpty())
    }

    @Test
    fun `getResponse returns fallback for unknown input`() {
        val response = AIAgent.getResponse("xyzzy frobozz zork")
        assertTrue(response.isNotEmpty())
        // Should contain some guidance
        assertTrue(response.contains("help") || response.contains("ask") || response.contains("don't"))
    }

    @Test
    fun `getSuggestedPrompts returns non-empty list`() {
        val prompts = AIAgent.getSuggestedPrompts()
        assertTrue(prompts.isNotEmpty())
    }
}
