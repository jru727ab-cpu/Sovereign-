package com.sovereign.wallet.ai

/**
 * Offline rule-based AI assistant.
 * Responds to common questions about the wallet app without requiring internet access.
 * Plug in an on-device LLM (e.g., MediaPipe LLM Inference or llama.cpp JNI) here later.
 */
object AIAgent {

    data class Message(val content: String, val isUser: Boolean)

    private val faqResponses: List<Pair<List<String>, String>> = listOf(
        listOf("hello", "hi", "hey") to
            "👋 Hi! I'm your Sovereign Wallet AI assistant. I'm fully offline and here to help.",

        listOf("help", "what can you do", "assist") to
            "I can help you with:\n• Creating or restoring a wallet\n• Understanding your backup phrase\n• Running bug cleaning\n• Changing settings\n• Explaining security features\n\nJust ask!",

        listOf("create wallet", "new wallet", "generate") to
            "To create a wallet:\n1. Tap 'Create New Wallet' on the home screen.\n2. Your 12-word backup phrase will be shown — WRITE IT DOWN!\n3. Store it somewhere safe offline (never digital).\n4. Your wallet address will appear on the overview screen.",

        listOf("restore", "recover", "import", "mnemonic") to
            "To restore a wallet:\n1. Go to the Restore screen.\n2. Enter your 12-word backup phrase exactly.\n3. Your wallet will be recovered and address shown.\n\n⚠️ Never share your phrase with anyone, including us.",

        listOf("backup", "phrase", "seed", "12 word") to
            "Your backup (seed) phrase is 12 words that represent your wallet. Keep it:\n• Written on paper (not digital)\n• In a secure location\n• Never photographed or typed online\n\nAnyone with your phrase has full access to your wallet.",

        listOf("send", "transfer", "payment") to
            "The Send screen lets you input an address and amount. For now, this is UI-ready for integration with live blockchain support in a future update.",

        listOf("receive", "deposit", "address") to
            "Your wallet address is shown on the Overview screen. Share it to receive funds. You can also copy it from the Receive tab.",

        listOf("bug", "clean", "cache", "slow", "fix", "maintenance") to
            "The Bug Cleaner can:\n• Clear app cache\n• Remove temp files\n• Run diagnostics\n\nGo to the 🔧 Bug Cleaner tab and tap 'Run Full Cleanup'.",

        listOf("pin", "password", "lock") to
            "Set a PIN in Settings to lock your wallet. Only the PIN hash is stored — never the raw PIN. You can also enable biometric (fingerprint/face) unlock.",

        listOf("biometric", "fingerprint", "face", "face id") to
            "Enable biometric unlock in Settings → Security. This allows fingerprint or face authentication to open the wallet quickly and securely.",

        listOf("security", "safe", "private", "encrypted") to
            "Your keys and mnemonic are encrypted with AES-256-GCM using Android Keystore. Data never leaves your device. No internet is required for core wallet functions.",

        listOf("settings", "theme", "dark", "light", "mode") to
            "In Settings you can:\n• Toggle dark/light mode\n• Enable/disable PIN\n• Enable/disable biometrics\n• View app diagnostics\n• Clear data",

        listOf("offline", "internet", "network", "online") to
            "Sovereign Wallet works 100% offline for core features. Your keys, mnemonic, and settings are stored locally. Live balances and transactions will require network access in a future update.",

        listOf("delete", "remove", "wipe", "reset") to
            "⚠️ Deleting your wallet removes all local data permanently. Make sure you have your 12-word backup phrase before deleting. Go to Settings → Delete Wallet.",

        listOf("version", "update", "latest") to
            "This is Sovereign Wallet v1.0.0 — your offline-first MVP. Updates will be available when you connect to a distribution channel.",

        listOf("thank", "thanks", "great", "awesome", "good") to
            "You're welcome! 😊 Stay sovereign, stay secure.",

        listOf("monetize", "premium", "pro", "upgrade", "subscription") to
            "Premium features are coming soon: advanced analytics, DApp browser, cloud backup, and more. They'll be available as optional in-app upgrades.",
    )

    /**
     * Process user input and return a contextual response.
     */
    fun getResponse(userInput: String): String {
        val input = userInput.trim().lowercase()
        if (input.isEmpty()) return "Please type a question or say 'help' to see what I can do!"

        for ((keywords, response) in faqResponses) {
            if (keywords.any { input.contains(it) }) {
                return response
            }
        }

        return "🤔 I don't have a specific answer for that yet, but I'm growing!\n\nTry asking about: wallet setup, backup phrase, security, bug cleaning, or settings.\n\nType 'help' for a full list."
    }

    /** Get suggested starter prompts for the UI. */
    fun getSuggestedPrompts(): List<String> = listOf(
        "How do I create a wallet?",
        "What is a backup phrase?",
        "How do I set a PIN?",
        "Run bug cleaner?",
        "How is my data secured?"
    )
}
