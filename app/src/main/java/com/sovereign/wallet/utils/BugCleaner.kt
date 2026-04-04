package com.sovereign.wallet.utils

import android.content.Context
import android.util.Log
import java.io.File

/**
 * App maintenance and bug cleaning utilities.
 * Clears cache, checks file integrity, and reports app health diagnostics.
 */
object BugCleaner {

    private const val TAG = "BugCleaner"

    /**
     * Clear all files in the app's cache directory.
     * @return true if all cleared successfully, false if any errors occurred.
     */
    fun clearCache(context: Context): Boolean {
        return try {
            val result = context.cacheDir.deleteRecursively()
            Log.d(TAG, "Cache cleared: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing cache", e)
            false
        }
    }

    /**
     * Clear external cache directory (if available).
     */
    fun clearExternalCache(context: Context): Boolean {
        return try {
            val extCacheDir = context.externalCacheDir
            extCacheDir?.deleteRecursively() ?: true
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing external cache", e)
            false
        }
    }

    /**
     * Remove any orphaned temp files from internal storage.
     */
    fun cleanTempFiles(context: Context): Int {
        var deleted = 0
        try {
            val filesDir = context.filesDir
            filesDir.listFiles()?.forEach { file ->
                if (file.name.endsWith(".tmp") || file.name.startsWith("_tmp_")) {
                    if (file.delete()) deleted++
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning temp files", e)
        }
        return deleted
    }

    /**
     * Run diagnostic checks and return a human-readable report.
     */
    fun runDiagnostics(context: Context): DiagnosticsReport {
        val issues = mutableListOf<String>()

        val cacheSizeMb = getCacheSizeBytes(context) / (1024.0 * 1024.0)
        if (cacheSizeMb > 50) {
            issues.add("Cache is large (%.1f MB) — consider clearing".format(cacheSizeMb))
        }

        val internalFree = context.filesDir.freeSpace / (1024 * 1024)
        if (internalFree < 10) {
            issues.add("Low internal storage: only ${internalFree}MB free")
        }

        val codeCacheDir = File(context.cacheDir, "code_cache")
        if (codeCacheDir.exists() && codeCacheDir.length() > 20 * 1024 * 1024) {
            issues.add("Code cache is large — consider clearing")
        }

        return DiagnosticsReport(
            cacheSize = cacheSizeMb,
            internalFreeSpaceMb = internalFree,
            issues = issues,
            status = if (issues.isEmpty()) "✅ App is healthy" else "⚠️ ${issues.size} issue(s) found"
        )
    }

    /** Get cache directory size in bytes. */
    fun getCacheSizeBytes(context: Context): Long =
        context.cacheDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }

    /** Get internal storage size used by app files in bytes. */
    fun getFilesStorageSizeBytes(context: Context): Long =
        context.filesDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }

    data class DiagnosticsReport(
        val cacheSize: Double,
        val internalFreeSpaceMb: Long,
        val issues: List<String>,
        val status: String
    )
}
