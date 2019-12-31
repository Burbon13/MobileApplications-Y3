package com.burbon13.planesmanager.planes.data

import android.util.Log
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.data.remote.PlaneDataSource
import com.burbon13.planesmanager.planes.model.Geolocation


class PlaneRepository(private val planeDataSource: PlaneDataSource) {
    suspend fun getPlane(tailNumber: String): Result<Plane> {
        Log.d(TAG, "Get plane with tailNumber=$tailNumber")
        return planeDataSource.getPlane(tailNumber)
    }

    suspend fun getAllPlanes(): Result<List<Plane>> {
        Log.d(TAG, "Get all planes")
        return planeDataSource.getPlanes()
    }

    suspend fun getPagePlanes(pageOffset: Int): Result<List<Plane>> {
        Log.d(TAG, "Get planes page with offset=$pageOffset")
        return planeDataSource.getPlanesPage(pageOffset)
    }

    suspend fun addPlane(plane: Plane): Result<Plane> {
        Log.d(TAG, "Add plane: $plane")
        return planeDataSource.addPlane(plane)
    }

    suspend fun getBrandsCount(): Result<Map<String, Int>> {
        Log.d(TAG, "Get brands count for all planes")
        return planeDataSource.getBrandsCount()
    }

    suspend fun deletePlane(tailNumber: String): Result<Boolean> {
        Log.d(TAG, "Deleting plane with tailNumber=$tailNumber")
        return planeDataSource.deletePlane(tailNumber)
    }

    suspend fun updatePlane(newPlane: Plane): Result<Plane> {
        Log.d(TAG, "Updating plane=$newPlane")
        return planeDataSource.updatePlane(newPlane)
    }

    suspend fun getPlaneGeolocation(tailNumber: String): Result<Geolocation> {
        Log.d(TAG, "Retrieving geolocation for plane with tailNumber=$tailNumber")
        return planeDataSource.getPlaneGeolocation(tailNumber)
    }
}
