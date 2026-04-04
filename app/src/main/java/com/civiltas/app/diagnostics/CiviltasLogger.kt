package com.civiltas.app.diagnostics

import com.civiltas.app.data.local.dao.DiagnosticLogDao
import com.civiltas.app.data.local.entity.DiagnosticLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CiviltasLogger @Inject constructor(
    private val diagnosticLogDao: DiagnosticLogDao
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun log(level: String, tag: String, message: String, t: Throwable? = null) {
        scope.launch {
            diagnosticLogDao.insert(
                DiagnosticLogEntity(
                    level = level,
                    tag = tag,
                    message = message,
                    stackTrace = t?.stackTraceToString()
                )
            )
            val cutoff = System.currentTimeMillis() - PRUNE_AGE_MILLIS
            diagnosticLogDao.pruneOlderThan(cutoff)
        }
    }

    companion object {
        private const val PRUNE_AGE_MILLIS = 7 * 24 * 60 * 60 * 1000L
    }

    fun info(tag: String, message: String) {
        Timber.tag(tag).i(message)
        log("INFO", tag, message)
    }

    fun warn(tag: String, message: String, t: Throwable? = null) {
        Timber.tag(tag).w(t, message)
        log("WARN", tag, message, t)
    }

    fun error(tag: String, message: String, t: Throwable? = null) {
        Timber.tag(tag).e(t, message)
        log("ERROR", tag, message, t)
    }
}
