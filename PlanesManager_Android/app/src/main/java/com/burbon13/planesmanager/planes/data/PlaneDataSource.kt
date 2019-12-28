package com.burbon13.planesmanager.planes.data

import android.util.Log
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.TAG
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.google.gson.JsonParser
import retrofit2.HttpException
import retrofit2.http.*


class PlaneDataSource {
    interface PlaneService {
        @Headers("Content-Type: application/json")
        @GET("/api/plane")
        suspend fun getPlanes(): List<Plane>

        @Headers("Content-Type: application/json")
        @GET("/api/plane/page/{page_offset}")
        suspend fun getPlanesPage(@Path("page_offset") pageOffset: Int): List<Plane>

        @Headers("Content-Type: application/json")
        @POST("/api/plane")
        suspend fun addPlane(@Body plane: Plane): Plane
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

    suspend fun getPlanesPage(pageOffset: Int): Result<List<Plane>> {
        Log.d(TAG, "Retrieving planes page with offset=$pageOffset")
        return try {
            val planesPage = planeService.getPlanesPage(pageOffset)
            Log.d(TAG, "Retrieved ${planesPage.size} planes for page with offset $pageOffset")
            Result.Success(planesPage)
        } catch (e: Exception) {
            Log.d(TAG, "Exception occurred while retrieving planes: ${e.message}")
            if (e is HttpException) {
                Result.Error(getApiErrorMessage(e))
            } else {
                Result.Error("An error occurred, contact support if this persists")
            }
        }
    }

    suspend fun addPlane(plane: Plane): Result<Plane> {
        Log.d(TAG, "Adding new plane to server: $plane")
        return try {
            Result.Success(planeService.addPlane(plane))
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Exception occurred while retrieving planes: ${e.message}")
            if (e is HttpException) {
                Result.Error(getApiErrorMessage(e))
            } else {
                Result.Error("An error occurred, contact support if this persist")
            }
        }
    }

    private fun getApiErrorMessage(e: HttpException): String {
        val errorJsonString = e.response()?.errorBody()?.string()
        return JsonParser().parse(errorJsonString)
            .asJsonObject.get("issue")
            .asJsonArray.get(0)
            .asJsonObject.get("error")
            .asString
    }
}
