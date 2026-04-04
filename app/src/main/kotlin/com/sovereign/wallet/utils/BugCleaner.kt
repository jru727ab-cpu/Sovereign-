package com.sovereign.wallet.utils

import android.content.Context
import android.util.Log
import java.io.File

/**
 * BugCleaner – modular maintenance utility.
 *
 * Responsibilities:
 *  - Clear the app cache directory to free up space.
 *  - Verify that key internal storage files and Room databases are intact.
 *  - Run lightweight diagnostics and return a human-readable report.
 *
 * All operations are local and offline-only.  Additional checks (APM, crash
 * log export, deep DB compaction, etc.) can be plugged in later by extending
 * [runAll] or adding new public functions.
 */
object BugCleaner {

    private const val TAG = "BugCleaner"

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Run every maintenance step and return a consolidated [CleanResult].
     * Safe to call from a coroutine on the IO dispatcher.
     */
    fun runAll(context: Context): CleanResult {
        Log.i(TAG, "Starting maintenance run")
        val cacheCleared = clearCache(context)
        val integrityReport = checkIntegrity(context)
        val diagnosticsReport = runDiagnostics(context)
        Log.i(TAG, "Maintenance run complete")
        return CleanResult(
            cacheCleared = cacheCleared,
            integrityReport = integrityReport,
            diagnosticsReport = diagnosticsReport
        )
    }

    /**
     * Delete all files in the app cache directory.
     * Returns `true` on success, `false` if any file could not be removed.
     */
    fun clearCache(context: Context): Boolean {
        return try {
            val deleted = context.cacheDir.deleteRecursively()
            Log.d(TAG, "Cache cleared: $deleted")
            deleted
        } catch (e: Exception) {
            Log.e(TAG, "Cache clear failed", e)
            false
        }
    }

    /**
     * Check that the internal files directory is readable and that any Room
     * database files are present and non-empty.
     *
     * Returns a short human-readable status string.
     */
    fun checkIntegrity(context: Context): String {
        val issues = mutableListOf<String>()

        // Verify internal files directory
        val filesDir: File = context.filesDir
        if (!filesDir.exists() || !filesDir.canRead()) {
            issues += "Internal storage directory is not accessible."
        }

        // Check every *.db file in the standard database directory
        val dbDir = File(context.filesDir.parent ?: "", "databases")
        if (dbDir.exists()) {
            dbDir.listFiles()?.filter { it.extension == "db" }?.forEach { db ->
                if (!db.canRead() || db.length() == 0L) {
                    issues += "Possible corruption in database: ${db.name}"
                }
            }
        }

        return if (issues.isEmpty()) {
            "File & database integrity: OK"
        } else {
            "Integrity issues found:\n" + issues.joinToString("\n• ", prefix = "• ")
        }
    }

    /**
     * Perform lightweight diagnostics (available storage, file counts, etc.)
     * and return a short status string.
     *
     * Expand this method over time to include log analysis, memory pressure
     * checks, APM metrics, and so on.
     */
    fun runDiagnostics(context: Context): String {
        val cacheSize = dirSize(context.cacheDir)
        val filesSize = dirSize(context.filesDir)
        val freeBytes = context.filesDir.freeSpace
        return buildString {
            appendLine("App diagnostics: OK")
            appendLine("Cache size   : ${formatBytes(cacheSize)}")
            appendLine("Files size   : ${formatBytes(filesSize)}")
            append("Free storage : ${formatBytes(freeBytes)}")
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private fun dirSize(dir: File): Long {
        if (!dir.exists()) return 0L
        return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    private fun formatBytes(bytes: Long): String = when {
        bytes >= 1_048_576L -> "%.1f MB".format(bytes / 1_048_576.0)
        bytes >= 1_024L     -> "%.1f KB".format(bytes / 1_024.0)
        else                -> "$bytes B"
    }
}

/**
 * Immutable data class holding the results of a full [BugCleaner.runAll] pass.
 */
data class CleanResult(
    val cacheCleared: Boolean,
    val integrityReport: String,
    val diagnosticsReport: String
)
