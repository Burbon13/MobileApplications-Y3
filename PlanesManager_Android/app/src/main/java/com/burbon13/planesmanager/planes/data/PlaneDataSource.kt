package com.burbon13.planesmanager.planes.data

import android.util.Log
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.utils.extensions.TAG
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

        @Headers("Content-Type: application/json")
        @GET("/api/plane/brands/count/{brands_list}")
        suspend fun getBrandsCount(@Path("brands_list") brandsList: String): Map<String, Int>
    }

    private val planeService: PlaneService = Api.retrofit.create(PlaneService::class.java)

    suspend fun getPlanes(): Result<List<Plane>> {
        Log.d(TAG, "Retrieving all planes")
        return try {
            Result.Success(planeService.getPlanes())
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorMessage = getApiErrorMessage(e)
                Log.d(TAG, "Exception occurred while retrieving planes: $errorMessage")
                Result.Error(errorMessage)
            } else {
                Log.d(TAG, "Exception occurred while retrieving planes: ${e.message}")
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
                val errorMessage = getApiErrorMessage(e)
                Log.d(TAG, "Exception occurred while retrieving planes: $errorMessage")
                Result.Error(errorMessage)
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
            if (e is HttpException) {
                val errorMessage = getApiErrorMessage(e)
                Log.d(TAG, "Exception occurred while adding planes $errorMessage")
                Result.Error(errorMessage)
            } else {
                Log.d(TAG, "Exception occurred while adding plane: ${e.message}")
                Result.Error("An error occurred, contact support if this persist")
            }
        }
    }

    suspend fun getBrandsCount(): Result<Map<String, Int>> {
        Log.d(TAG, "Retrieving all brand counts")
        return try {
            Result.Success(planeService.getBrandsCount(Plane.BrandList.joinToString(separator = ",")))
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Exception occurred while retrieving all brand counts: ${e.message}")
            Result.Error("An error occurred, contact support if this persists")
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
