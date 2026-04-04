# Proguard rules for Sovereign Wallet
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Keep security crypto classes
-keep class androidx.security.crypto.** { *; }

# Keep biometric classes
-keep class androidx.biometric.** { *; }

# Keep wallet data models
-keep class com.sovereign.wallet.data.** { *; }
