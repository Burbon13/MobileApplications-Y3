package com.burbon13.planesmanager.planes.ui.plane

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.data.PlaneRepository
import com.burbon13.planesmanager.planes.model.Geolocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaneDataViewModel : ViewModel() {

    private val _plane = MutableLiveData<Result<Plane>>()
    val plane: LiveData<Result<Plane>>
        get() = _plane

    private val _planeDeletionResult = MutableLiveData<Result<Boolean>>()
    val planeDeletionResult: LiveData<Result<Boolean>>
        get() = _planeDeletionResult

    private val _planeGeolocationResult = MutableLiveData<Result<Geolocation>>()
    val planeGeolocationResult: LiveData<Result<Geolocation>>
        get() = _planeGeolocationResult

    fun loadPlane(tailNumber: String) {
        Log.d(TAG, "Loading plane with tailNumber=$tailNumber")
        viewModelScope.launch(Dispatchers.IO) {
            val plane = PlaneRepository.getPlane(tailNumber)
            if (plane != null) {
                _plane.postValue(Result.Success(plane))
            } else {
                _plane.postValue(Result.Error("Error loading plane"))
            }
        }
    }

    fun deletePlane(tailNumber: String) {
        Log.d(TAG, "Deleting plane with tailNumber=$tailNumber")
        viewModelScope.launch(Dispatchers.IO) {
            _planeDeletionResult.postValue(PlaneRepository.deletePlane(tailNumber))
        }
    }

    fun updatePlane(newPlane: Plane) {
        Log.d(TAG, "Updating plane=$newPlane")
        viewModelScope.launch(Dispatchers.IO) {
            _plane.postValue(PlaneRepository.updatePlane(newPlane))
        }
    }

    fun getPlaneGeolocation(tailNumber: String) {
        Log.d(TAG, "Loading geolocation for tailNumber=$tailNumber")
        viewModelScope.launch(Dispatchers.IO) {
            _planeGeolocationResult.postValue(PlaneRepository.getPlaneGeolocation(tailNumber))
        }
    }
}
