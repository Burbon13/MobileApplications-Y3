package com.burbon13.planesmanager.planes.data

import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result


class PlaneRepository(private val planeDataSource: PlaneDataSource) {
    suspend fun getAllPlanes(): Result<List<Plane>> {
        return planeDataSource.getPlanes()
    }

    suspend fun getPagePlanes(pageOffset: Int): Result<List<Plane>> {
        return planeDataSource.getPlanesPage(pageOffset)
    }

    suspend fun addPlane(plane: Plane): Result<Plane> {
        return planeDataSource.addPlane(plane)
    }
}