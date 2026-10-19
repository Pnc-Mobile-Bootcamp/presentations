package com.pnc.jetpackcomposedemos.features.auth.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class SessionManagerTest {

    private val user = User(id = "11000", username = "Test User", token = "t")

    @Test
    fun `starts out unauthenticated`() {
        val session = SessionManager()

        assertEquals(AuthState.Unauthenticated, session.state.value)
        assertNull(session.currentUser)
    }

    @Test
    fun `signing in exposes the user`() {
        val session = SessionManager()

        session.signIn(user)

        assertEquals(AuthState.Authenticated(user), session.state.value)
        assertEquals(user, session.currentUser)
    }

    @Test
    fun `signing out clears the user`() {
        val session = SessionManager()
        session.signIn(user)

        session.signOut()

        assertEquals(AuthState.Unauthenticated, session.state.value)
        assertNull(session.currentUser)
    }
}