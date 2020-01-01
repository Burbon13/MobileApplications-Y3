package com.burbon13.planesmanager.planes.data.remote

import android.util.Log
import com.burbon13.planesmanager.core.Api
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.api.ApiUtils
import com.burbon13.planesmanager.planes.model.Geolocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Headers
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PUT


object PlaneDataSource {
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

        @Headers("Content-Type: application/json")
        @DELETE("/api/plane/{tail_number}")
        suspend fun deletePlane(@Path("tail_number") tailNumber: String): Response<Unit>

        @Headers("Content-Type: application/json")
        @PUT("/api/plane")
        suspend fun updatePlane(@Body plane: Plane): Plane

        @Headers("Content-Type: application/json")
        @GET("/api/plane/position/{tail_number}")
        suspend fun getPlaneGeolocation(@Path("tail_number") tailNumber: String): Geolocation
    }

    private val planeService: PlaneService = Api.retrofit.create(
        PlaneService::class.java
    )

    suspend fun getPlane(tailNumber: String): Result<Plane> {
        Log.d(TAG, "Retrieving plane with tailNumber=$tailNumber")
        return ApiUtils.retrieveResult(
            { planeService.getPlane(tailNumber) },
            "Exception occurred while retrieving plane with tailNumber=$tailNumber",
            "An error occurred"
        )
    }

    suspend fun getPlanes(): Result<List<Plane>> {
        Log.d(TAG, "Retrieving all planes")
        return ApiUtils.retrieveResult(
            { planeService.getPlanes() },
            "Exception occurred while retrieving all planes",
            "An error occurred"
        )
    }

    suspend fun getPlanesPage(pageOffset: Int): Result<List<Plane>> {
        Log.d(TAG, "Retrieving planes page with offset=$pageOffset")
        return ApiUtils.retrieveResult(
            { planeService.getPlanesPage(pageOffset) },
            "Exception occurred while retrieving planes page=$pageOffset",
            "An error occurred"
        )
    }

    suspend fun addPlane(plane: Plane): Result<Plane> {
        Log.d(TAG, "Adding new plane=$plane")
        return ApiUtils.retrieveResult(
            { planeService.addPlane(plane) },
            "Exception occurred while saving new plane=$plane",
            "An error occurred"
        )
    }

    suspend fun getBrandsCount(): Result<Map<String, Int>> {
        Log.d(TAG, "Retrieving all brand counts")
        return ApiUtils.retrieveResult(
            { planeService.getBrandsCount(Plane.BrandList.joinToString(separator = ",")) },
            "Exception occurred while retrieving brand counts",
            "An error occurred"
        )
    }

    suspend fun deletePlane(tailNumber: String): Result<Boolean> {
        Log.d(TAG, "Deleting plane with tailNumber=$tailNumber")
        return ApiUtils.retrieveResult(
            {
                planeService.deletePlane(tailNumber)
                true
            },
            "Exception occurred while deleting plane with tailNumber=$tailNumber",
            "An error occurred"
        )
    }

    suspend fun updatePlane(newPlane: Plane): Result<Plane> {
        Log.d(TAG, "Updating plane=$newPlane")
        return ApiUtils.retrieveResult(
            { planeService.updatePlane(newPlane) },
            "Exception occurred while updating plane=$newPlane",
            "An error occurred"
        )
    }

    suspend fun getPlaneGeolocation(tailNumber: String): Result<Geolocation> {
        Log.d(TAG, "Retrieving geolocation for plane with tailNumber=$tailNumber")
        return ApiUtils.retrieveResult(
            { planeService.getPlaneGeolocation(tailNumber) },
            "Exception occurred while retrieving plane geolocation with tailNumber=$tailNumber",
            "An error occurred"
        )
    }
}
