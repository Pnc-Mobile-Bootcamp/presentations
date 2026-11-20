package com.pnc.jetpackcomposedemos.core.observability

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object HttpLogging {

    // an HTTP logger that never prints the headers that carry credentials
    // (without redactHeader, the logging function writes
    // "Authorization: Bearer <the real token>" for every request, straight into LogCat)
    fun interceptor(
        level: HttpLoggingInterceptor.Level,
        logger: HttpLoggingInterceptor.Logger = HttpLoggingInterceptor.Logger {
            message ->
            Timber.tag("OkHttp").d(message)
        }
    ) : HttpLoggingInterceptor = HttpLoggingInterceptor(logger).apply {
        this.level = level
        redactHeader("Authorization")
        redactHeader("Cookie")
        redactHeader("Set-Cookie")
    }

}


