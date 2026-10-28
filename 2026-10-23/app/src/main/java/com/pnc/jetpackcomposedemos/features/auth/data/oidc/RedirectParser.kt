package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import java.net.URI
import java.net.URLDecoder

object RedirectParser {

    data class Parsed(
        val code: String?,
        val state: String?,
        val error: String?,
        val errorDescription: String?
    )

    fun parse(redirect: String): Parsed {
        val query = URI(redirect).rawQuery.orEmpty()
        val params = query.split('&')
            .filter { it.isNotEmpty() }
            .associate { pair ->    // string like:  "key=value"
                val parts = pair.split('=')
                val key = URLDecoder.decode(parts[0], "UTF-8")
                val value = URLDecoder.decode(parts[1], "UTF-8")
                key to value
            }
        return Parsed(
            code = params["code"],
            state = params["state"],
            error = params["error"],
            errorDescription = params["error_description"]
        )
    }

}


