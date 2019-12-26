package com.burbon13.planesmanager.auth.data

import com.burbon13.planesmanager.auth.data.model.User
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.Result


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
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = dataSource.login(user)
        if (result is Result.Success) {
            setLoggedInUser(user, result.data)
        }
        return result
    }

    private fun setLoggedInUser(loggedInUser: User, tokenHolder: TokenHolder) {
        this.user = loggedInUser
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
