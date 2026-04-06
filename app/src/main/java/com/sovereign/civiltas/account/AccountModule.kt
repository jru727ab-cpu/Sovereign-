package com.sovereign.civiltas.account

interface AccountModule {
    fun isLoggedIn(): Boolean
    fun getUserId(): String?
    fun getDisplayName(): String
    fun signOut()
}
