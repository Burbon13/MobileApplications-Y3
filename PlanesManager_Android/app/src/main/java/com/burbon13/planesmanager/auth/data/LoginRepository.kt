package com.burbon13.planesmanager.auth.data

import android.util.Log
import com.burbon13.planesmanager.auth.data.model.User
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class LoginRepository(private val dataSource: LoginDataSource) {
    private var user: User? = null
    val isLoggedIn: Boolean
        get() = user != null

    fun logout() {
        Log.d(TAG, "User ${user?.username} logged out")
        user = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        Log.d(TAG, "Login with credentials username=$username and password=$password")
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
        Log.d(TAG, "Setting token for user ${loggedInUser.username}")
        this.user = loggedInUser
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
