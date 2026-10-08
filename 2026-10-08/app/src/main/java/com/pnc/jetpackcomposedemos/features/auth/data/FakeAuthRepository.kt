package com.pnc.jetpackcomposedemos.features.auth.data

import com.pnc.jetpackcomposedemos.features.auth.domain.AuthRepository
import com.pnc.jetpackcomposedemos.features.auth.domain.User

class FakeAuthRepository: AuthRepository {

    override suspend fun login(userId: String, passcode: String): Result<User> {

        if (userId == "hello" && passcode == "world") {
            return Result.success(User(
                id = "abc",
                username = "hello",
                token = "good for 1 free pizza"
            ))
        }

        return Result.failure(IllegalArgumentException("Invalid credentials"))

    }

}