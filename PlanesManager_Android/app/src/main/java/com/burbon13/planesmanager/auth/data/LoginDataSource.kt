package com.burbon13.planesmanager.auth.data

import android.util.Log
import com.burbon13.planesmanager.auth.data.model.User
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.TAG
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.lang.Exception


/**
 * Class that handles authentication w/ login credentials and retrieves the session token.
 */
class LoginDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): Result<TokenHolder> {
        Log.d(TAG, "$user wants to login")
        return try {
            Result.Success(authService.login(user))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
