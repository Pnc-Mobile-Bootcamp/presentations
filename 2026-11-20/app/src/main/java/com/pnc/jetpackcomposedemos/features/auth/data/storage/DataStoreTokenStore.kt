package com.pnc.jetpackcomposedemos.features.auth.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

@Singleton
class DataStoreTokenStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val crypto: KeystoreCrypto
): TokenStore {

    private val userKey = stringPreferencesKey("user")

    override suspend fun save(user: User) {
        val userString = Json.encodeToString(User.serializer(), user)
        val encrypted = crypto.encrypt(userString)
        context.sessionDataStore.edit { it[userKey] = encrypted }
    }

    override suspend fun load(): User? {
        val encoded = context.sessionDataStore.data.first()[userKey] ?: return null
        return try {
            Json.decodeFromString(User.serializer(), crypto.decrypt(encoded))
        } catch (e: Exception) {
            // unreadable - treat as logged out
            clear()
            null
        }
    }

    override suspend fun clear() {
        context.sessionDataStore.edit { it.remove(userKey) }
    }


}




