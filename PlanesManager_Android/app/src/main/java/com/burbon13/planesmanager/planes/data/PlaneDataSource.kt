package com.burbon13.planesmanager.planes.data

import android.util.Log
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.TAG
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.google.gson.JsonParser
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Headers


class PlaneDataSource {
    interface PlaneService {
        @Headers("Content-Type: application/json")
        @GET("/api/plane")
        suspend fun getPlanes(): List<Plane>
    }

    private val planeService: PlaneService = Api.retrofit.create(PlaneService::class.java)

    suspend fun getPlanes(): Result<List<Plane>> {
        Log.d(TAG, "Retrieving all planes")
        return try {
            Result.Success(planeService.getPlanes())
        } catch (e: Exception) {
            Log.d(TAG, "Exception occurred while retrieving planes: ${e.message}")
            if (e is HttpException) {
                val errorJsonString = e.response()?.errorBody()?.string()
                val errorMessage =
                    JsonParser().parse(errorJsonString)
                        .asJsonObject.get("issue")
                        .asJsonArray.get(0)
                        .asJsonObject.get("error")
                        .asString
                Result.Error(errorMessage)
            } else {
                Result.Error("An error occurred, contact support if this persists")
            }
        }
    }
}
