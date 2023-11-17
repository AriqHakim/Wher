package id.emergence.wher.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("prefs")

class DataStoreManager(
    appContext: Context,
) {
    private val prefsDataStore = appContext.dataStore

    // auth token
    val token: Flow<String>
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[Keys.SESSION_TOKEN_KEY] ?: ""
            }

    suspend fun addSession(token: String) {
        prefsDataStore.edit { prefs ->
            prefs[Keys.SESSION_TOKEN_KEY] = "Bearer $token"
        }
    }

    suspend fun clearSession() {
        prefsDataStore.edit { prefs ->
            prefs[Keys.SESSION_TOKEN_KEY] = ""
        }
    }

    val isLoggedIn: Flow<Boolean>
        get() =
            prefsDataStore.data.map { prefs ->
                (prefs[Keys.SESSION_TOKEN_KEY] ?: "").isNotEmpty()
            }
}
