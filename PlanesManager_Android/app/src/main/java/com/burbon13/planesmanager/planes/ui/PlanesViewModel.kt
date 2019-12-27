package com.burbon13.planesmanager.planes.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.planesmanager.planes.data.PlaneDataSource
import com.burbon13.planesmanager.planes.data.PlaneRepository
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlanesViewModel : ViewModel() {
    private val planeRepository = PlaneRepository(PlaneDataSource())

    private val _planeMutableLiveDate = MutableLiveData<List<Plane>>()
    val planeLiveData: LiveData<List<Plane>>
        get() = _planeMutableLiveDate

    private val _toastMessageMutableLiveData = MutableLiveData<String>()
    val toastMessageLiveData: LiveData<String>
        get() = _toastMessageMutableLiveData

    init {
        Log.d(TAG, "Initializing PlanesViewModel")
        viewModelScope.launch {
            var planesResult: Result<List<Plane>>? = null
            withContext(Dispatchers.IO) {
                Log.d(TAG, "Retrieving planes on Dispatchers.IO")
                planesResult = planeRepository.getAllPlanes()
                Log.d(TAG, "Planes retrieved")
            }
            if (planesResult is Result.Success) {
                Log.d(TAG, "Planes retrieved successfully")
                _planeMutableLiveDate.value = (planesResult as Result.Success<List<Plane>>).data
                _toastMessageMutableLiveData.value = "Planes loaded successfully"
            } else if (planesResult is Result.Error) {
                Log.d(
                    TAG,
                    "Planes retrieval resulted in an error: " +
                            (planesResult as Result.Error).message
                )
                _toastMessageMutableLiveData.value = "Error occurred while loading planes"
            }
        }
    }
}
