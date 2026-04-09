package com.sovereign.civiltas.account

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalGuestAccountModule @Inject constructor() : AccountModule {
    override fun isLoggedIn(): Boolean = false
    override fun getUserId(): String? = null
    override fun getDisplayName(): String = "Guest Sovereign"
    override fun signOut() { /* no-op for guest */ }
}
