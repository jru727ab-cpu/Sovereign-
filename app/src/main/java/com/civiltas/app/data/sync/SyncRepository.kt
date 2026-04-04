package com.civiltas.app.data.sync

/**
 * Sync strategy: LOCAL-FIRST
 *
 * All writes go to local Room DB first. Background sync pushes deltas to
 * the remote (when available). On conflict, local data wins unless the
 * remote timestamp is strictly newer and the player has not made local
 * progress since the last successful sync.
 *
 * Future/Backlog: DeNet decentralised storage
 * - Use DeNet Storage SDK to store encrypted player state as a blob on the
 *   DeNet network (IPFS-compatible, user-owned storage).
 * - Each player's progress is stored at their own DeNet address.
 * - This removes server-side storage costs and gives players true ownership.
 * - Reference: https://denet.pro/
 */
interface SyncRepository {
    /** Upload local snapshot to remote. No-op when offline. */
    suspend fun pushToRemote()

    /** Pull the latest remote snapshot and merge into local. */
    suspend fun pullFromRemote()

    /** Returns true if a valid auth session exists. */
    fun isLoggedIn(): Boolean
}
