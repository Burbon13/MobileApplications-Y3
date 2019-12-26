package com.burbon13.planesmanager.auth.data

import android.util.Log
import com.burbon13.planesmanager.auth.data.model.User
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.TAG


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class LoginRepository(val dataSource: LoginDataSource) {
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        Log.d(TAG, "User ${user?.username} logged out")
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = dataSource.login(user)
        if (result is Result.Success) {
            Log.d(TAG, "User $username logged in successfully")
            setLoggedInUser(user, result.data)
        } else {
            Log.d(TAG, "User $username login failed: ${(result as Result.Error).message}")
        }
        return result
    }

    private fun setLoggedInUser(loggedInUser: User, tokenHolder: TokenHolder) {
        Log.d(TAG, "Setting token for user ${user?.username}")
        this.user = loggedInUser
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
