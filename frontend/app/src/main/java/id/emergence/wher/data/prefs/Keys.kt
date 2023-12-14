package id.emergence.wher.data.prefs

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Keys {
    val SESSION_TOKEN_KEY = stringPreferencesKey("session_token")
    val SESSION_USER_ID = stringPreferencesKey("user_id")

    val SESSION_IS_SHARING_LOCATION = booleanPreferencesKey("is_sharing_location")
    val SESSION_LAST_SHARING_TIME = stringPreferencesKey("last_sharing_time")
}
