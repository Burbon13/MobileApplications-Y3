package com.burbon13.planesmanager.auth.data.local

import android.content.Context
import android.util.Log
import com.burbon13.planesmanager.core.MyApp
import com.burbon13.planesmanager.core.utils.extensions.TAG


object LoginSharedPreferences {
    private val sharedPreferences = MyApp.context?.getSharedPreferences(
        "com.burbon13.planesmanager.shared.pref",
        Context.MODE_PRIVATE
    )

    fun getSessionToken(): String? {
        val sessionToken = sharedPreferences?.getString("session_token", null)
        Log.d(TAG, "Retrieving session token from shared preferences: $sessionToken")
        return sessionToken
    }

    fun saveSessionToken(sessionToken: String) {
        Log.d(TAG, "Saving session token in shared preferences")
        sharedPreferences?.edit()?.putString("session_token", sessionToken)?.apply()
    }

    fun deleteSessionToken() {
        Log.d(TAG, "Deleting session token")
        sharedPreferences?.edit()?.remove("session_token")?.apply()
    }
}
