package com.sovereign.wallet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.sovereign.wallet.viewmodels.WalletViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val MAX_PIN_ATTEMPTS = 5
private const val LOCKOUT_DURATION_MS = 30_000L // 30 seconds

@Composable
fun LockScreen(viewModel: WalletViewModel) {
    val isPinEnabled by viewModel.isPinEnabled.collectAsState()
    val isBiometricEnabled by viewModel.isBiometricEnabled.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var pinInput by remember { mutableStateOf("") }
    var showPin by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var attempts by remember { mutableStateOf(0) }
    var isLockedOut by remember { mutableStateOf(false) }
    var lockoutSecondsRemaining by remember { mutableStateOf(0) }

    fun startLockout() {
        isLockedOut = true
        lockoutSecondsRemaining = (LOCKOUT_DURATION_MS / 1000).toInt()
        errorMessage = null
        coroutineScope.launch {
            while (lockoutSecondsRemaining > 0) {
                delay(1000L)
                lockoutSecondsRemaining--
            }
            isLockedOut = false
            attempts = 0
            errorMessage = null
        }
    }

    fun tryBiometric() {
        val activity = context as? FragmentActivity ?: return
        val executor = ContextCompat.getMainExecutor(context)
        val biometricManager = BiometricManager.from(context)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
            errorMessage = "Biometric not available on this device."
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Sovereign Wallet")
            .setSubtitle("Use biometrics to access your wallet")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    viewModel.unlock()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    errorMessage = "Authentication error: $errString"
                }

                override fun onAuthenticationFailed() {
                    errorMessage = "Authentication failed. Try again."
                }
            }
        )
        biometricPrompt.authenticate(promptInfo)
    }

    // Auto-prompt biometrics on first display
    LaunchedEffect(isBiometricEnabled) {
        if (isBiometricEnabled) tryBiometric()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Sovereign Wallet",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Enter your PIN to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            if (isPinEnabled) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        if (isLockedOut) {
                            // Show lockout countdown
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🔒 Too many failed attempts. Try again in ${lockoutSecondsRemaining}s.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        } else {
                            OutlinedTextField(
                                value = pinInput,
                                onValueChange = {
                                    if (it.length <= 8) {
                                        pinInput = it
                                        errorMessage = null
                                    }
                                },
                                label = { Text("Enter PIN") },
                                visualTransformation = if (showPin) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                isError = errorMessage != null,
                                supportingText = errorMessage?.let { msg ->
                                    { Text(msg, color = MaterialTheme.colorScheme.error) }
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showPin = !showPin }) {
                                        Icon(
                                            if (showPin) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            )

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (viewModel.verifyPin(pinInput)) {
                                        viewModel.unlock()
                                    } else {
                                        attempts++
                                        if (attempts >= MAX_PIN_ATTEMPTS) {
                                            startLockout()
                                        } else {
                                            val remaining = MAX_PIN_ATTEMPTS - attempts
                                            errorMessage = "Incorrect PIN. $remaining attempt(s) remaining."
                                        }
                                        pinInput = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Unlock", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (isBiometricEnabled) {
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { tryBiometric() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Fingerprint, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Use Biometrics")
                }
            }

            if (!isPinEnabled && !isBiometricEnabled) {
                // No security configured — this shouldn't appear, but fallback
                Button(
                    onClick = { viewModel.unlock() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Open Wallet")
                }
            }
        }
    }
}
