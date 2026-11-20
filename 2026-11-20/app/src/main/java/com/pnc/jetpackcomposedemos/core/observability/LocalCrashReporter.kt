package com.pnc.jetpackcomposedemos.core.observability

import android.content.Context
import android.os.Build
import com.pnc.jetpackcomposedemos.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

// Write each report to a text file in the app's private storage
// and keep only the newest few.
@Singleton
class LocalCrashReporter @Inject constructor(
    @ApplicationContext private val context: Context
) : CrashReporter {

    private val directory: File
        get() = File(context.filesDir, "crash-reports")

    private val breadcrumbs = ArrayDeque<String>()

    private companion object {
        const val MAX_REPORTS = 10
        const val MAX_BREADCRUMBS = 50
    }

    private fun prune() {
        directory.listFiles()
            ?.sortedByDescending { it.lastModified() }
            ?.drop(MAX_REPORTS)
            ?.forEach { it.delete() }
    }

    private fun compose(kind: String, throwable: Throwable, message: String?): String = buildString {
        appendLine("kind: $kind")
        appendLine("time: ${Instant.now()}")
        appendLine("app: ${BuildConfig.VERSION_NAME} (${BuildConfig.BUILD_TYPE})")
        appendLine("device: ${Build.MANUFACTURER} ${Build.MODEL}, Android ${Build.VERSION.SDK_INT}")
        if (message != null) appendLine("message: $message")
        appendLine()
        appendLine(StringWriter().also { throwable.printStackTrace(PrintWriter(it)) }.toString())
        appendLine("breadcrumbs:")
        synchronized(breadcrumbs) {
            breadcrumbs.forEach { appendLine("  $it") }
        }
    }

    private fun write(kind: String, throwable: Throwable, message: String?) {

        try {
            directory.mkdirs()
            val file = File(directory, "report-${System.currentTimeMillis()}-$kind.txt")
            file.writeText(LogRedactor.scrub(compose(kind, throwable, message)))
            prune()
        } catch (ignored: Exception) {
            // reporting must NEVER be the thing that crashes the app
        }

    }

    // Interface methods
    override fun log(message: String) {
        synchronized(breadcrumbs) {
            breadcrumbs.addLast("${Instant.now()} ${LogRedactor.scrub(message)}")
            while (breadcrumbs.size > MAX_BREADCRUMBS) breadcrumbs.removeFirst()
        }
    }

    override fun recordException(throwable: Throwable, message: String?) {
        write(kind = "non-fatal", throwable = throwable, message = message)
    }

    override fun recordFatal(throwable: Throwable) {
        write(kind = "fatal", throwable = throwable, message = null)
    }

    override fun latestReport(): String? =
        directory.listFiles()
            ?.maxByOrNull { it.lastModified() }
            ?.readText()


}




