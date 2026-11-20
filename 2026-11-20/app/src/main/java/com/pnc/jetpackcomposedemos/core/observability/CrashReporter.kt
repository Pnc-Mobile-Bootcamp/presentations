package com.pnc.jetpackcomposedemos.core.observability


// Where problems go
// The app talks to this interface only, so the destination can change
// (a file today, a crash-reporting service tomorrow)
// without touching any caller
interface CrashReporter {

    // a short note that will be attached to the next report
    fun log(message: String)

    // a problem the app handled and carried on from
    // (a failed refresh, a rejected token, etc.)
    fun recordException(throwable: Throwable, message: String? = null)

    // the app is about to die from an exception nobody caught
    fun recordFatal(throwable: Throwable)

    // the text of the newest report, or null when there are none
    fun latestReport(): String?

}

