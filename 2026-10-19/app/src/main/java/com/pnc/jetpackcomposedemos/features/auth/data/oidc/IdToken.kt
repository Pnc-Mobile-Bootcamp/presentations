package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64

object IdToken {

    data class Claims(
        val subject: String,   // "sub": the CustomerID
        val name: String?,     // "name": only present because we asked for the "profile" scope
        val nonce: String?
    )

    /**
     * A JWT is three base64url parts separated by dots: header.payload.signature.
     * We only need the middle one.
     *
     * We do NOT verify the signature here. That is allowed by the OpenID Connect spec because we
     * received this token directly from the token endpoint over HTTPS.
     */
    fun parse(jwt: String): Claims {
        val payload = jwt.split('.').getOrNull(1)
            ?: throw IllegalArgumentException("The ID token is not a JWT.")
        val json = Json.parseToJsonElement(
            String(Base64.getUrlDecoder().decode(payload), Charsets.UTF_8)
        ).jsonObject

        return Claims(
            subject = json["sub"]?.jsonPrimitive?.contentOrNull
                ?: throw IllegalArgumentException("The ID token has no subject."),
            name = json["name"]?.jsonPrimitive?.contentOrNull,
            nonce = json["nonce"]?.jsonPrimitive?.contentOrNull
        )
    }
}



