package id.emergence.wher.data.prefs

import androidx.datastore.preferences.core.stringPreferencesKey

object Keys {
    val SESSION_TOKEN_KEY = stringPreferencesKey("session_token")
    val SESSION_USER_ID = stringPreferencesKey("user_id")
}
