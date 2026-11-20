package com.pnc.jetpackcomposedemos.core.observability

import android.util.Log
import timber.log.Timber

class CrashReportingTree(
    private val reporter: CrashReporter
) : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int) = priority >= Log.WARN

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        reporter.log("${tag ?: "app"}: $message")

        if (t != null && priority >= Log.ERROR) {
            reporter.recordException(t, message)
        }

    }

}

