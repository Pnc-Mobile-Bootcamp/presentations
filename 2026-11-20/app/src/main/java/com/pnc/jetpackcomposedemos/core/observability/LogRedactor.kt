package com.pnc.jetpackcomposedemos.core.observability

// removes the things that must never reach a log file or crash report:
// bearer tokens, JWTs, secrets in query strings and JSON, email addresses
object LogRedactor {

    private val rules: List<Pair<Regex, String>> = listOf(
        // Authorization: Bearer abc.def
        Regex("""(?i)\bBearer\s+[A-Za-z0-9._~+/=-]+""") to "Bearer [redacted]",
        // a JWT on its own: three base64url parts, the first starting with eyJ (which is "{" encoded)
        Regex("""\beyJ[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+\.[A-Za-z0-9_-]*""") to "[jwt]",
        // ?code=abc&refresh_token=def (query strings and form bodies)
        Regex("""(?i)\b(access_token|refresh_token|id_token|token|code|code_verifier|client_secret|password)=([^&\s]+)""") to "$1=[redacted]",
        // "refresh_token":"abc" (JSON bodies)
        Regex("""(?i)"(access_token|refresh_token|id_token|password)"\s*:\s*"[^"]*"""") to "\"$1\":\"[redacted]\"",
        // someone@example.com
        Regex("""[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}""") to "[email]"
    )

    fun scrub(text: String): String =
        rules.fold(text) { current, (pattern, replacement) ->
            pattern.replace(current, replacement)
        }

}


