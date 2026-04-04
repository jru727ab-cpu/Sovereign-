package com.civiltas.app.data.sync

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Placeholder implementation – replace with real HTTP/DeNet calls once
 * a backend / DeNet endpoint is available.
 */
@Singleton
class LocalFirstSyncImpl @Inject constructor() : SyncRepository {

    private var _isLoggedIn = false

    override suspend fun pushToRemote() {
        Timber.d("SyncRepository: pushToRemote – stub, no remote configured yet")
        // TODO: serialize Room snapshot → JSON → POST to backend / DeNet
    }

    override suspend fun pullFromRemote() {
        Timber.d("SyncRepository: pullFromRemote – stub, no remote configured yet")
        // TODO: GET from backend / DeNet → deserialize → merge with local-wins strategy
    }

    override fun isLoggedIn(): Boolean = _isLoggedIn

    fun login(token: String) {
        _isLoggedIn = token.isNotBlank()
        Timber.i("SyncRepository: login stub – loggedIn=$_isLoggedIn")
    }

    fun logout() {
        _isLoggedIn = false
    }
}
