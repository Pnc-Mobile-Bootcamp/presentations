package com.pnc.jetpackcomposedemos.testutil.builders

import com.pnc.jetpackcomposedemos.features.auth.domain.User

fun aUser(
    id: String = "user-1",
    username: String = "testuser",
    token: String = "test-token"
) = User(id, username, token)

