package com.burbon13.template.myobjects.data.remote

import android.util.Log
import com.burbon13.template.core.Api
import com.burbon13.template.core.ApiUtils
import com.burbon13.template.core.MyResult
import com.burbon13.template.core.extensions.TAG
import com.burbon13.template.myobjects.model.MyObject
import retrofit2.Response
import retrofit2.http.*


object MyObjectDataSource {
    private const val myObjectName = "/myObject"

    interface MyObjectService {
        @Headers("Content-Type: application/json")
        @GET(myObjectName)
        suspend fun getMyObjects(): List<MyObject>

        @Headers("Content-Type: application/json")
        @GET("$myObjectName/{id}")
        suspend fun getMyObject(@Path("id") id: String): MyObject

        @Headers("Content-Type: application/json")
        @GET("$myObjectName/page/{page_offset}")
        suspend fun getMyObjectsPage(@Path("page_offset") pageOffset: Int): List<MyObject>

        @Headers("Content-Type: application/json")
        @POST(myObjectName)
        suspend fun addMyObject(@Body myObject: MyObject): MyObject

        @Headers("Content-Type: application/json")
        @DELETE("$myObjectName/{id}")
        suspend fun deleteMyObject(@Path("id") id: String): Response<Unit>

        @Headers("Content-Type: application/json")
        @PUT(myObjectName)
        suspend fun updateMyObject(@Body myObject: MyObject): MyObject
    }

    private val myObjectService: MyObjectService = Api.retrofit.create(
        MyObjectService::class.java
    )

    suspend fun getMyObject(id: String): MyResult<MyObject> {
        Log.d(TAG, "Retrieving myObject with id=$id")
        return ApiUtils.retrieveResult(
            { myObjectService.getMyObject(id) },
            "Exception occurred while retrieving myObject with id=$id",
            "An error occurred while retrieving myObject"
        )
    }

    suspend fun getMyObjects(): MyResult<List<MyObject>> {
        Log.d(TAG, "Retrieving all myObjects")
        return ApiUtils.retrieveResult(
            { myObjectService.getMyObjects() },
            "Exception occurred while retrieving all myObjects",
            "An error occurred while retrieving all myObjects"
        )
    }

    suspend fun getMyObjectsPage(pageOffset: Int): MyResult<List<MyObject>> {
        Log.d(TAG, "Retrieving myObjects page with offset=$pageOffset")
        return ApiUtils.retrieveResult(
            { myObjectService.getMyObjectsPage(pageOffset) },
            "Exception occurred while retrieving myObjects page=$pageOffset",
            "Exception occurred while retrieving myObjects page"
        )
    }

    suspend fun addMyObject(myObject: MyObject): MyResult<MyObject> {
        Log.d(TAG, "Adding new myObject=$myObject")
        return ApiUtils.retrieveResult(
            { myObjectService.addMyObject(myObject) },
            "Exception occurred while saving new myObject=$myObject",
            "An error occurred while saving new myObject"
        )
    }

    suspend fun deleteMyObject(id: String): MyResult<Boolean> {
        Log.d(TAG, "Deleting plane with id=$id")
        return ApiUtils.retrieveResult(
            {
                myObjectService.deleteMyObject(id)
                true
            },
            "Exception occurred while deleting myObject with id=$id",
            "An error occurred while deleting myObject"
        )
    }

    suspend fun updatePlane(myObject: MyObject): MyResult<MyObject> {
        Log.d(TAG, "Updating plane=$myObject")
        return ApiUtils.retrieveResult(
            { myObjectService.updateMyObject(myObject) },
            "Exception occurred while updating myObject=$myObject",
            "An error occurred while updating myObject"
        )
    }
}
