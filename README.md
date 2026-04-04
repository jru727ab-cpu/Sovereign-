# Sovereign Wallet — Android MVP

An **offline-first, privacy-focused** Android crypto wallet with encrypted key storage, bug cleaning utilities, PIN/biometric security, AI assistant, and a modular architecture ready for Play Store.

---

## 🔐 Features

| Feature | Status |
|---|---|
| Offline-first wallet (create/restore) | ✅ Complete |
| AES-256-GCM encrypted key storage | ✅ Complete |
| 12-word BIP39-style mnemonic generation | ✅ Complete |
| PIN protection (SHA-256 hashed) | ✅ Complete |
| Biometric unlock (fingerprint/face) | ✅ Complete |
| Bug cleaner (cache, temp files, diagnostics) | ✅ Complete |
| Offline AI assistant | ✅ Complete |
| Send/Receive UI (blockchain stub) | ✅ UI Ready |
| Dark/light mode toggle | ✅ Complete |
| Internal file storage (DeNet-inspired) | ✅ Complete |
| Live blockchain balance | 🔧 Future Update |
| Cloud backup | 🔧 Future Update |
| Monetization (in-app purchases) | 🔧 Future Update |

---

## 🏗️ Project Structure

```
app/src/main/java/com/sovereign/wallet/
├── MainActivity.kt              # App entry point + navigation
├── ai/
│   └── AIAgent.kt               # Offline rule-based AI assistant
├── data/
│   ├── WalletStorage.kt         # Encrypted key/mnemonic storage
│   └── LocalFileManager.kt      # Internal file storage (DeNet-inspired)
├── security/
│   └── Security.kt              # PIN hashing + biometric settings
├── utils/
│   ├── WalletUtils.kt           # Wallet generation + address derivation
│   └── BugCleaner.kt            # Cache clearing + diagnostics
├── viewmodels/
│   └── WalletViewModel.kt       # State management (ViewModel + StateFlow)
└── ui/
    ├── navigation/Screen.kt      # Nav routes + bottom nav items
    ├── theme/                    # Material You colors, typography, theme
    └── screens/
        ├── WalletScreen.kt       # Wallet overview + backup phrase
        ├── SendReceiveScreen.kt  # Send/Receive tabs
        ├── BugCleanerScreen.kt   # Maintenance + diagnostics
        ├── AIHelperScreen.kt     # Chat-style AI assistant
        ├── SettingsScreen.kt     # PIN, biometrics, dark mode, about
        ├── RestoreScreen.kt      # Wallet restore from mnemonic
        └── LockScreen.kt        # PIN/biometric lock screen
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1) or newer
- JDK 17
- Android SDK 26+

### Build & Run
```bash
# Clone and open in Android Studio
# OR build from command line:
./gradlew assembleDebug

# Install on connected device:
./gradlew installDebug
```

### Run Unit Tests
```bash
./gradlew test
```

---

## 🔒 Security Architecture

- **Keys never leave the device** — stored in `EncryptedSharedPreferences` backed by Android Keystore
- **PINs are never stored** — only SHA-256 hashes
- **No internet permissions** — core wallet is fully air-gapped
- **Backup is excluded** from Android Cloud Backup to prevent key leakage
- **Network security config** disables all cleartext traffic

---

## 🗺️ Roadmap

1. **v1.0** (Current) — Offline MVP: wallet, security, AI, bug cleaner, settings
2. **v1.1** — Blockchain node integration (Ethereum/Bitcoin live balances)
3. **v1.2** — QR code generation for receive addresses
4. **v1.3** — Multi-wallet support
5. **v2.0** — Cloud encrypted backup (optional, user-controlled)
6. **v2.1** — In-app purchases, premium features

---

## 📋 Play Store Checklist

- [x] No unnecessary permissions
- [x] Privacy Policy screen (built-in)
- [x] No deceptive behavior
- [x] No cleartext network traffic
- [x] Encrypted local storage
- [x] Backup exclusions configured
- [ ] Privacy Policy URL (add before submission)
- [ ] App signing key generated
- [ ] In-app purchase integration (future)
