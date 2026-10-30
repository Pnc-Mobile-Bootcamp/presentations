package com.pnc.jetpackcomposedemos.features.auth.domain

import com.pnc.jetpackcomposedemos.features.auth.data.storage.FakeTokenStore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SessionManagerTest {

    private val user = User(id = "11000", username = "Test User", token = "t", refreshToken = "r")

    // UnconfinedTestDispatcher runs the "save" side effect immediately, so tests don't need to wait.
    @Test
    fun `starts out restoring`() = runTest(UnconfinedTestDispatcher()) {
        val session = SessionManager(FakeTokenStore(), backgroundScope)

        assertEquals(AuthState.Restoring, session.state.value)
    }

    @Test
    fun `restore signs in a saved user`() = runTest(UnconfinedTestDispatcher()) {
        val session = SessionManager(FakeTokenStore(saved = user), backgroundScope)

        session.restore()

        assertEquals(AuthState.Authenticated(user), session.state.value)
    }

    @Test
    fun `restore with nothing saved ends up unauthenticated`() = runTest(UnconfinedTestDispatcher()) {
        val session = SessionManager(FakeTokenStore(), backgroundScope)

        session.restore()

        assertEquals(AuthState.Unauthenticated, session.state.value)
        assertNull(session.currentUser)
    }

    @Test
    fun `signing in saves the user`() = runTest(UnconfinedTestDispatcher()) {
        val store = FakeTokenStore()
        val session = SessionManager(store, backgroundScope)

        session.signIn(user)

        assertEquals(user, store.saved)
        assertEquals(user, session.currentUser)
    }

    @Test
    fun `updating replaces the saved user`() = runTest(UnconfinedTestDispatcher()) {
        val store = FakeTokenStore()
        val session = SessionManager(store, backgroundScope)
        session.signIn(user)

        session.update(user.copy(token = "new-token"))

        assertEquals("new-token", store.saved?.token)
    }

    @Test
    fun `signing out forgets the user`() = runTest(UnconfinedTestDispatcher()) {
        val store = FakeTokenStore(saved = user)
        val session = SessionManager(store, backgroundScope)
        session.signIn(user)

        session.signOut()

        assertNull(store.saved)
        assertNull(session.currentUser)
        assertEquals(AuthState.Unauthenticated, session.state.value)
    }

    @Test
    fun `restore does nothing once someone has already signed in`() = runTest(UnconfinedTestDispatcher()) {
        val session = SessionManager(FakeTokenStore(saved = null), backgroundScope)
        session.signIn(user)

        session.restore()

        assertEquals(user, session.currentUser)
    }
}



