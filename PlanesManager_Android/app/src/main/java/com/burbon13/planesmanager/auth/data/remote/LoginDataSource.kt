package com.burbon13.planesmanager.auth.data.remote

import android.util.Log
import com.burbon13.planesmanager.auth.data.TokenHolder
import com.burbon13.planesmanager.auth.data.User
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.google.gson.JsonParser
import retrofit2.HttpException
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.lang.Exception


/**
 * Class that handles authentication with login credentials and retrieves the session token.
 */
object LoginDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(
        AuthService::class.java)

    suspend fun login(user: User): Result<TokenHolder> {
        Log.d(TAG, "$user wants to login")
        return try {
            Result.Success(authService.login(user))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorJsonString = e.response()?.errorBody()?.string()
                val errorMessage =
                    JsonParser().parse(errorJsonString)
                        .asJsonObject.get("issue")
                        .asJsonArray.get(0)
                        .asJsonObject.get("error")
                        .asString
                Log.d(TAG, "Exception occurred while logging in $user: $errorMessage")
                Result.Error(errorMessage)
            } else {
                Log.d(TAG, "Exception occurred while logging in $user: ${e.message}")
                Result.Error("Authentication error occurred")
            }
        }
    }
}