package com.sovereign.civiltas.diagnostics

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object BugReporter {

    fun exportAndShare(context: Context) {
        val logs = CiviltasLogger.exportLogs()
        val deviceInfo = buildString {
            appendLine("CIVILTAS Bug Report")
            appendLine("===================")
            appendLine("Device: ${android.os.Build.MODEL}")
            appendLine("Android: ${android.os.Build.VERSION.RELEASE}")
            appendLine("App Version: ${context.packageManager
                .getPackageInfo(context.packageName, 0).versionName}")
            appendLine()
            appendLine("--- Logs ---")
            append(logs)
        }

        val file = File(context.cacheDir, "civiltas_bug_report.txt")
        file.writeText(deviceInfo)

        val uri = FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "CIVILTAS Bug Report")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Bug Report"))
    }
}
