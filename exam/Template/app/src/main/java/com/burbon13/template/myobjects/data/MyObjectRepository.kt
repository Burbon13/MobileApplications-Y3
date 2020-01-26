package com.burbon13.template.myobjects.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.burbon13.template.core.MyApp
import com.burbon13.template.core.MyResult
import com.burbon13.template.core.extensions.TAG
import com.burbon13.template.myobjects.data.local.MyObjectDatabase
import com.burbon13.template.myobjects.data.remote.MyObjectDataSource
import com.burbon13.template.myobjects.model.MyObject
import java.lang.Exception


object MyObjectRepository {
    private val myObjectDao = MyObjectDatabase.getDatabase(
        MyApp.context ?: throw Exception("Unable to retrieve Application context")
    ).myObjectDao()

    suspend fun getPlane(id: String): MyObject? {
        Log.d(TAG, "Get myObject with id=$id")
        return myObjectDao.getById(id)
    }

    fun getAllPlanes(): LiveData<List<MyObject>> {
        Log.d(TAG, "Get all myObjects")
        return myObjectDao.getAll()
    }

    private const val PAGE_SIZE = 15
    suspend fun getPagePlanes(pageOffset: Int): List<MyObject> {
        Log.d(TAG, "Get myObjects page with offset=$pageOffset")
        return myObjectDao.getPage(PAGE_SIZE, PAGE_SIZE * pageOffset)
    }

    suspend fun addMyObject(myObject: MyObject): MyResult<MyObject> {
        Log.d(TAG, "Add myObject: $myObject")
        return try {
            val addMyObjectResult = MyObjectDataSource.addMyObject(myObject)
            if (addMyObjectResult is MyResult.Success) {
                myObjectDao.insert(addMyObjectResult.data)
            }
            addMyObjectResult
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred while adding new myObject: ${e.message}")
            MyResult.Error("An error occurred while adding new myObject")
        }
    }

    suspend fun deleteMyObject(id: String): MyResult<Boolean> {
        Log.d(TAG, "Deleting myObject with id=$id")
        return try {
            val deletionResult = MyObjectDataSource.deleteMyObject(id)
            if (deletionResult is MyResult.Success) {
                myObjectDao.delete(id)
            }
            deletionResult
        } catch (e: Exception) {
            Log.d(TAG, "Deletion error while deleting myObject: ${e.message}")
            MyResult.Error("An error occurred while deleting myObject")
        }
    }

    suspend fun updateMyObject(myObject: MyObject): MyResult<MyObject> {
        Log.d(TAG, "Updating myObject=$myObject")
        return try {
            val updateResult = MyObjectDataSource.updateMyObject(myObject)
            if (updateResult is MyResult.Success) {
                myObjectDao.update(myObject)
            }
            updateResult
        } catch (e: Exception) {
            Log.d(TAG, "Update error while updating myObject: ${e.message}")
            MyResult.Error("An error occurred while updating myObject")
        }
    }
}
