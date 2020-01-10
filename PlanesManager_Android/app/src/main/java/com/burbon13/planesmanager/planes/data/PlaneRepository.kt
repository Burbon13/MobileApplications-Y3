package com.burbon13.planesmanager.planes.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.burbon13.planesmanager.core.MyApp
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.data.local.PlanesDatabase
import com.burbon13.planesmanager.planes.data.remote.PlaneDataSource
import com.burbon13.planesmanager.planes.model.Geolocation
import java.lang.Exception


object PlaneRepository {
    private val planeDao = PlanesDatabase.getDatabase(
        MyApp.context ?: throw Exception("Unable to retrieve Application context")
    ).planeDao()

    suspend fun refresh(): Result<Boolean> {
        Log.d(TAG, "Synchronizing data from remote location")
        return try {
            val planesResult = PlaneDataSource.getPlanes()
            if (planesResult is Result.Success) {
                planeDao.deleteAll()
                val planes = planesResult.data
                for (plane in planes) {
                    planeDao.insert(plane)
                }
                Result.Success(true)
            }
            planesResult as Result.Error
        } catch (e: Exception) {
            Log.d(TAG, "Exception occurred: ${e.message}")
            Result.Error("Error occurred while synchronizing data")
        }
    }

    suspend fun getPlane(tailNumber: String): Plane? {
        Log.d(TAG, "Get plane with tailNumber=$tailNumber")
        return planeDao.getByTailNumber(tailNumber)
    }

    suspend fun getAllPlanes(): LiveData<List<Plane>> {
        Log.d(TAG, "Get all planes")
        return planeDao.getAll()
    }

    suspend fun getPagePlanes(pageOffset: Int): Result<List<Plane>> {
        Log.d(TAG, "Get planes page with offset=$pageOffset")
        return Result.Success(planeDao.getPage(15, 15 * pageOffset))
    }

    suspend fun addPlane(plane: Plane): Result<Plane> {
        Log.d(TAG, "Add plane: $plane")
        return try {
            val addPlaneResult = PlaneDataSource.addPlane(plane)
            if (addPlaneResult is Result.Success) {
                planeDao.insert(addPlaneResult.data)
            }
            addPlaneResult
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred: ${e.message}")
            Result.Error("An error occurred")
        }
    }

    suspend fun getBrandsCount(): Result<Map<String, Int>> {
        Log.d(TAG, "Get brands count for all planes")
        val map = HashMap<String, Int>()
        for (brand in Plane.Brand.values()) {
            map[brand.toString()] = planeDao.getBrandCount(brand.toString())
        }
        return Result.Success(map)
    }

    suspend fun deletePlane(tailNumber: String): Result<Boolean> {
        Log.d(TAG, "Deleting plane with tailNumber=$tailNumber")
        return try {
            val deletionResult = PlaneDataSource.deletePlane(tailNumber)
            if (deletionResult is Result.Success) {
                planeDao.delete(tailNumber)
            }
            deletionResult
        } catch (e: Exception) {
            Log.d(TAG, "Deletion error: ${e.message}")
            Result.Error("An error occurred while deleting plane")
        }
    }

    suspend fun updatePlane(newPlane: Plane): Result<Plane> {
        Log.d(TAG, "Updating plane=$newPlane")
        return try {
            val updateResult = PlaneDataSource.updatePlane(newPlane)
            if (updateResult is Result.Success) {
                planeDao.update(newPlane)
            }
            updateResult
        } catch (e: Exception) {
            Log.d(TAG, "Update error: ${e.message}")
            Result.Error("An error occurred while updating plane")
        }
    }

    suspend fun getPlaneGeolocation(tailNumber: String): Result<Geolocation> {
        Log.d(TAG, "Retrieving geolocation for plane with tailNumber=$tailNumber")
        return PlaneDataSource.getPlaneGeolocation(tailNumber)
    }
}
