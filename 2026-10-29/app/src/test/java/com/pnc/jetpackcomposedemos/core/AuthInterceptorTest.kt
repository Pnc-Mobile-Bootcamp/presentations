package com.pnc.jetpackcomposedemos.core

import com.pnc.jetpackcomposedemos.BuildConfig
import com.pnc.jetpackcomposedemos.features.auth.data.storage.FakeTokenStore
import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test


class AuthInterceptorTest {

    private val session = SessionManager(
        FakeTokenStore(),
        CoroutineScope(Dispatchers.Unconfined)
    )
    private val seen = mutableListOf<Request>()

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(session))
        .addInterceptor(Interceptor { chain ->
            seen += chain.request()
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body("".toResponseBody())
                .build()
        })
        .build()

    private fun call(url: String) {
        client.newCall(Request.Builder().url(url).build()).execute().close()
    }

    @Test
    fun `adds the bearer token for our API`() {
        session.signIn(User(id = "1", username = "A", token = "secret-token"))

        call(BuildConfig.AW_BASE_URL + "my/customers/1/orders")

        assertEquals("Bearer secret-token", seen.single().header("Authorization"))
    }

    @Test
    fun `sends nothing when signed out`() {
        call(BuildConfig.AW_BASE_URL + "Department")

        assertNull(seen.single().header("Authorization"))
    }

    @Test
    fun `never sends the token to another host`() {
        session.signIn(User(id = "1", username = "A", token = "secret-token"))

        call("https://some-other-server.example.com/api/artists")

        assertNull(seen.single().header("Authorization"))
    }
}




