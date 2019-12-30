package com.burbon13.planesmanager.planes.data

import android.util.Log
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.google.gson.JsonParser
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Headers
import retrofit2.http.Body
import retrofit2.http.POST


class PlaneDataSource {
    interface PlaneService {
        @Headers("Content-Type: application/json")
        @GET("/api/plane")
        suspend fun getPlanes(): List<Plane>

        @Headers("Content-Type: application/json")
        @GET("/api/plane/{tail_number}")
        suspend fun getPlane(@Path("tail_number") tailNumber: String): Plane

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

    suspend fun getPlane(tailNumber: String): Result<Plane> {
        Log.d(TAG, "Retrieving plane with tailNumber=$tailNumber")
        return try {
            Result.Success(planeService.getPlane(tailNumber))
        } catch (e: HttpException) {
            val errorMessage = getApiErrorMessage(e)
            Log.e(
                TAG,
                "Exception occurred while retrieving plane with tailNumber=$tailNumber: $errorMessage"
            )
            Result.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Exception occurred while retrieving plane with tailNumber=$tailNumber: ${e.message}"
            )
            Result.Error("An error occurred, contact support if this persists")
        }
    }

    suspend fun getPlanes(): Result<List<Plane>> {
        Log.d(TAG, "Retrieving all planes")
        return try {
            Result.Success(planeService.getPlanes())
        } catch (e: HttpException) {
            val errorMessage = getApiErrorMessage(e)
            Log.e(TAG, "Exception occurred while retrieving planes: $errorMessage")
            Result.Error(errorMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Exception occurred while retrieving planes: ${e.message}")
            Result.Error("An error occurred, contact support if this persists")
        }
    }


    suspend fun getPlanesPage(pageOffset: Int): Result<List<Plane>> {
        Log.d(TAG, "Retrieving planes page with offset=$pageOffset")
        return try {
            val planesPage = planeService.getPlanesPage(pageOffset)
            Log.d(TAG, "Retrieved ${planesPage.size} planes for page with offset $pageOffset")
            Result.Success(planesPage)
        } catch (e: HttpException) {
            val errorMessage = getApiErrorMessage(e)
            Log.e(
                TAG,
                "Exception occurred while retrieving page with offset $pageOffset: $errorMessage"
            )
            Result.Error(errorMessage)
        } catch (e: java.lang.Exception) {
            Log.e(
                TAG,
                "Exception occurred while retrieving page with offset $pageOffset: ${e.message}"
            )
            Result.Error("An error occurred, contact support if this persists")
        }
    }

    suspend fun addPlane(plane: Plane): Result<Plane> {
        Log.d(TAG, "Adding new plane to server: $plane")
        return try {
            Result.Success(planeService.addPlane(plane))
        } catch (e: HttpException) {
            val errorMessage = getApiErrorMessage(e)
            Log.e(TAG, "Exception occurred while adding new plane=$plane: $errorMessage")
            Result.Error(errorMessage)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception occurred while adding new plane=$plane: ${e.message}")
            Result.Error("An error occurred, contact support if this persists")
        }
    }

    suspend fun getBrandsCount(): Result<Map<String, Int>> {
        Log.d(TAG, "Retrieving all brand counts")
        return try {
            Result.Success(planeService.getBrandsCount(Plane.BrandList.joinToString(separator = ",")))
        } catch (e: HttpException) {
            val errorMessage = getApiErrorMessage(e)
            Log.e(TAG, "Exception occurred while retrieving brand counts: $errorMessage")
            Result.Error(errorMessage)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception occurred while retrieving brand counts: ${e.message}")
            Result.Error("An error occurred, contact support if this persists")
        }
    }

    private fun getApiErrorMessage(e: HttpException): String {
        val errorJsonString = e.response()?.errorBody()?.string()
        return try {
            JsonParser().parse(errorJsonString)
                .asJsonObject.get("issue")
                .asJsonArray.get(0)
                .asJsonObject.get("error")
                .asString
        } catch (e: java.lang.Exception) {
            "SERVER ERROR"
        }
    }
}
