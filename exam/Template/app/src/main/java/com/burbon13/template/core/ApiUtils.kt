package com.burbon13.template.core

import android.util.Log
import com.burbon13.template.core.extensions.TAG
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.lang.Exception

/**
 * Class which contains various utils functions for the API
 */
object ApiUtils {
    private const val DEFAULT_ERROR_MESSAGE = "Something went wrong"

    /**
     * Executes an API call and wraps the result into an Result (Result.Success or Result.Error)
     * @param apiCall the API call to be executed
     * @param logErrorMessage the desired message to be logged in case of error (will be followed by
     * the exception message)
     * @param resultErrorMessage the desired message to be put into the Result.Error in case no
     * explanation is given by the API call failure)
     * @return Result.Success if the API call was successful with the result inside, Result.Error
     * otherwise with the error message inside
     */
    suspend fun <T : Any> retrieveResult(
        apiCall: suspend () -> T,
        logErrorMessage: String,
        resultErrorMessage: String
    ): MyResult<T> {
        return try {
            MyResult.Success(apiCall())
        } catch (e: HttpException) {
            val errorMessage = extractErrorMessage(e)
            Log.e(TAG, "$logErrorMessage: $errorMessage")
            MyResult.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(TAG, "$logErrorMessage: ${e.message}")
            MyResult.Error(resultErrorMessage)
        }
    }

    /**
     * Extracts the error from an error response given by the API
     * @param e the exception thrown by the API call
     * @param defaultMessage message to be returned in case no data can be extracted from the
     * exception
     */
    private fun extractErrorMessage(
        e: HttpException,
        defaultMessage: String = DEFAULT_ERROR_MESSAGE
    ): String {
        return try {
            val errorJsonString = e.response()?.errorBody()?.string()
            // TODO: Customize based on the server error response
            JsonParser().parse(errorJsonString)
                .asJsonObject.get("issue")
                .asJsonArray.get(0)
                .asJsonObject.get("error")
                .asString
        } catch (e: Exception) {
            defaultMessage
        }
    }
}

