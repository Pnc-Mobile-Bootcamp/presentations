package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("connect/token")
    suspend fun exchangeCode(
        @Field("grant_type") grantType: String,        // "authorization_code"
        @Field("client_id") clientId: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code_verifier") codeVerifier: String   // <- the proof only our app has
    ): TokenResponse

    @FormUrlEncoded
    @POST("connect/token")
    suspend fun refresh(
        @Field("grant_type") grantType: String,        // "refresh_token"
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String
    ): TokenResponse
}


