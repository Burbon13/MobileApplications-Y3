package com.burbon13.planesmanager.planes.data

import android.util.Log
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG


class PlaneRepository(private val planeDataSource: PlaneDataSource) {
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
}