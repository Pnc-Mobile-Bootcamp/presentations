package com.pnc.jetpackcomposedemos.features.auth.domain

interface AuthRepository {

    suspend fun login(userId: String, passcode: String): Result<User>

}


