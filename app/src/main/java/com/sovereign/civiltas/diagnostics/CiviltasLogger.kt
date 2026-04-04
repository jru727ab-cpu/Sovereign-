package com.sovereign.civiltas.diagnostics

import android.util.Log

object CiviltasLogger {
    private const val TAG = "CIVILTAS"
    private const val MAX_LOG_ENTRIES = 200
    private val buffer = ArrayDeque<String>(MAX_LOG_ENTRIES)

    fun d(msg: String) { log("D", msg) }
    fun i(msg: String) { log("I", msg) }
    fun w(msg: String) { log("W", msg) }
    fun e(msg: String, throwable: Throwable? = null) {
        log("E", msg + (throwable?.let { ": ${it.message}" } ?: ""))
        throwable?.let { Log.e(TAG, msg, it) }
    }

    private fun log(level: String, msg: String) {
        val entry = "[$level] ${System.currentTimeMillis()} $msg"
        if (buffer.size >= MAX_LOG_ENTRIES) buffer.removeFirst()
        buffer.addLast(entry)
        Log.d(TAG, msg)
    }

    fun exportLogs(): String = buffer.joinToString("\n")
    fun clearLogs() = buffer.clear()
}
