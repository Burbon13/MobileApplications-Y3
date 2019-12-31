package com.burbon13.planesmanager.auth.data

import android.util.Log
import com.burbon13.planesmanager.auth.data.local.LoginSharedPreferences
import com.burbon13.planesmanager.auth.data.remote.LoginDataSource
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
object LoginRepository {
    private val TOKEN_KEY = "com.burbon13.planesmanager.token"

    /**
     * Verifies if a session token is saved
     * @return the session token if exits, null otherwise
     */
    fun userLoggedIn(): String? {
        Log.d(TAG, "Checking if token is saved in shared preferences")
        return LoginSharedPreferences.getSessionToken()
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        Log.d(TAG, "Login with credentials username=$username and password=$password")
        val user = User(username, password)
        val result = LoginDataSource.login(user)
        if (result is Result.Success) {
            Log.d(TAG, "User $username logged in successfully")
            setLoggedInUser(result.data)
        } else {
            Log.d(TAG, "User $username login failed: ${(result as Result.Error).message}")
        }
        return result
    }

    fun setLoggedInUser(tokenHolder: TokenHolder) {
        Api.tokenInterceptor.token = tokenHolder.token
        Log.d(TAG, "Saving token ${tokenHolder.token} to shared preferences")
        LoginSharedPreferences.saveSessionToken(tokenHolder.token)
    }

    fun logout() {
        Log.d(TAG, "Logout")
        LoginSharedPreferences.deleteSessionToken()
    }
}
