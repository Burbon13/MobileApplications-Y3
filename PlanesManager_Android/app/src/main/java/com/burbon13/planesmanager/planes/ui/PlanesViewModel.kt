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
        _planeMutableLiveDate.value = listOf()
        appendPlanesPage(0)
    }

    fun appendPlanesPage(pageOffset: Int) {
        Log.d(TAG, "Append planes page with offset=$pageOffset")
        viewModelScope.launch {
            var newPlanesResult: Result<List<Plane>>? = null
            withContext(Dispatchers.IO) {
                newPlanesResult = planeRepository.getPagePlanes(pageOffset)
            }
            if (newPlanesResult is Result.Success) {
                Log.d(TAG, "Planes page with offset=$pageOffset retrieved successfully")
                _planeMutableLiveDate.value =
                    _planeMutableLiveDate.value?.plus((newPlanesResult as Result.Success<List<Plane>>).data)
            } else if (newPlanesResult is Result.Error) {
                Log.d(
                    TAG,
                    "Planes retrieval resulted in an error: " +
                            (newPlanesResult as Result.Error).message
                )
                _toastMessageMutableLiveData.value = "Error occurred while loading planes"
            }
        }
    }
}
