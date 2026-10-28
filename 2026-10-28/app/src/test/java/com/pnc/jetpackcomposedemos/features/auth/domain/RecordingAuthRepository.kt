package com.pnc.jetpackcomposedemos.features.auth.domain

import com.pnc.jetpackcomposedemos.testutil.builders.aUser


class RecordingAuthRepository(
    private val result: Result<User> = Result.success(aUser())
): AuthRepository {

    var loginCalls = 0
        private set

    var lastUserId: String? = null
        private set

    var lastPasscode: String? = null
        private set

    override suspend fun login(userId: String, passcode: String): Result<User> {
        loginCalls++
        lastUserId = userId
        lastPasscode = passcode
        return result
    }

}

