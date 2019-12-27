package com.burbon13.planesmanager.planes.data

import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result


class PlaneRepository(private val planeDataSource: PlaneDataSource) {
    suspend fun getAllPlanes(): Result<List<Plane>> {
        return planeDataSource.getPlanes()
    }
}