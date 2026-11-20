package com.pnc.jetpackcomposedemos.features.auth.domain

import com.pnc.jetpackcomposedemos.features.auth.data.FakeAuthRepository

class LoginUseCase(
  private val repository: AuthRepository = FakeAuthRepository()
) {
    // using the "invoke" operator allows calling instances of the class, like a function
    suspend operator fun invoke(userId: String, passcode: String): Result<User> {

        if (userId.isBlank() || passcode.isBlank()) {
            return Result.failure(IllegalArgumentException("Credentials cannot be empty"))
        }

        if (passcode.length < 5) {
            return Result.failure(IllegalArgumentException("Passcode must be at least 5 characters"))
        }

        return repository.login(userId, passcode)

    }

}


