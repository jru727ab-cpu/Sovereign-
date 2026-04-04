package com.sovereign.wallet

import com.sovereign.wallet.utils.WalletUtils
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for offline wallet utilities.
 */
class WalletUtilsTest {

    @Test
    fun `generateMnemonic returns 12 words`() {
        val mnemonic = WalletUtils.generateMnemonic()
        val words = mnemonic.split(" ")
        assertEquals(12, words.size)
    }

    @Test
    fun `generateMnemonic words are non-empty`() {
        val mnemonic = WalletUtils.generateMnemonic()
        assertTrue(mnemonic.split(" ").all { it.isNotBlank() })
    }

    @Test
    fun `mnemonicToAddress returns valid 0x address`() {
        val mnemonic = WalletUtils.generateMnemonic()
        val address = WalletUtils.mnemonicToAddress(mnemonic)
        assertTrue(address.startsWith("0x"))
        assertEquals(42, address.length) // 0x + 40 hex chars
    }

    @Test
    fun `mnemonicToAddress is deterministic`() {
        val mnemonic = "abandon ability able about above absent absorb abstract absurd abuse access accident"
        val address1 = WalletUtils.mnemonicToAddress(mnemonic)
        val address2 = WalletUtils.mnemonicToAddress(mnemonic)
        assertEquals(address1, address2)
    }

    @Test
    fun `different mnemonics produce different addresses`() {
        val mnemonic1 = WalletUtils.generateMnemonic()
        val mnemonic2 = WalletUtils.generateMnemonic()
        // Extremely unlikely to be the same
        if (mnemonic1 != mnemonic2) {
            assertNotEquals(
                WalletUtils.mnemonicToAddress(mnemonic1),
                WalletUtils.mnemonicToAddress(mnemonic2)
            )
        }
    }

    @Test
    fun `isValidMnemonic returns true for valid 12-word phrase`() {
        val mnemonic = "abandon ability able about above absent absorb abstract absurd abuse access accident"
        assertTrue(WalletUtils.isValidMnemonic(mnemonic))
    }

    @Test
    fun `isValidMnemonic returns false for phrase with wrong word count`() {
        val mnemonic = "abandon ability able about above absent"
        assertFalse(WalletUtils.isValidMnemonic(mnemonic))
    }

    @Test
    fun `isValidMnemonic returns false for phrase with invalid words`() {
        val mnemonic = "aaaa bbbbb ccccc ddddd eeeee fffff ggggg hhhhh iiiii jjjjj kkkkk lllll"
        assertFalse(WalletUtils.isValidMnemonic(mnemonic))
    }

    @Test
    fun `abbreviateAddress returns shortened form`() {
        val address = "0x1234567890abcdef1234567890abcdef12345678"
        val abbreviated = WalletUtils.abbreviateAddress(address)
        assertTrue(abbreviated.contains("..."))
        assertTrue(abbreviated.startsWith("0x1234"))
    }

    @Test
    fun `abbreviateAddress returns short address unchanged`() {
        val short = "0x12345"
        assertEquals(short, WalletUtils.abbreviateAddress(short))
    }
}
