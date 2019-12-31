package com.burbon13.planesmanager.auth.data.local

import android.content.Context
import android.util.Log
import com.burbon13.planesmanager.core.MyApp
import com.burbon13.planesmanager.core.utils.extensions.TAG


/**
 * Class which stores the session token used for the API calls
 */
object LoginSharedPreferences {
    private const val SESSION_TOKEN_KEY = "session_token"
    private const val SHARED_PREFERENCES_ID = "com.burbon13.planesmanager.shared.pref"

    private val sharedPreferences = MyApp.context?.getSharedPreferences(
        SHARED_PREFERENCES_ID,
        Context.MODE_PRIVATE
    ) ?: throw LoginSharedPreferencesException("Cannot access shared preferences")

    /**
     * @return the session token if exists, null otherwise
     */
    fun getSessionToken(): String? {
        val sessionToken = sharedPreferences.getString(SESSION_TOKEN_KEY, null)
        Log.d(TAG, "Retrieved session token from shared preferences: $sessionToken")
        return sessionToken
    }

    fun saveSessionToken(sessionToken: String) {
        Log.d(TAG, "Saving session token in shared preferences: $sessionToken")
        sharedPreferences.edit().putString(SESSION_TOKEN_KEY, sessionToken).apply()
    }

    fun deleteSessionToken() {
        Log.d(TAG, "Deleting session token")
        sharedPreferences.edit().remove(SESSION_TOKEN_KEY).apply()
    }
}
