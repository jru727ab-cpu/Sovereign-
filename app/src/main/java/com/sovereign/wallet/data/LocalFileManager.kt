package com.sovereign.wallet.data

import android.content.Context
import java.io.File
import java.io.IOException

/**
 * Internal file storage manager (DeNet-inspired local storage).
 * All files are stored in app's private internal storage — no external read/write needed.
 */
object LocalFileManager {

    /** Save arbitrary bytes to internal storage. */
    fun saveFile(context: Context, filename: String, content: ByteArray): Boolean {
        return try {
            val file = File(context.filesDir, filename)
            file.writeBytes(content)
            true
        } catch (e: IOException) {
            false
        }
    }

    /** Read bytes from internal storage. Returns null if file doesn't exist. */
    fun readFile(context: Context, filename: String): ByteArray? {
        val file = File(context.filesDir, filename)
        return if (file.exists()) file.readBytes() else null
    }

    /** Delete a specific file from internal storage. */
    fun deleteFile(context: Context, filename: String): Boolean {
        val file = File(context.filesDir, filename)
        return if (file.exists()) file.delete() else false
    }

    /** List all files in internal storage. */
    fun listFiles(context: Context): List<String> =
        context.filesDir.listFiles()?.map { it.name } ?: emptyList()

    /** Get total size (bytes) used by internal storage files. */
    fun getTotalStorageUsedBytes(context: Context): Long =
        context.filesDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }

    /** Get total size used by cache directory. */
    fun getCacheSizeBytes(context: Context): Long =
        context.cacheDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
}
