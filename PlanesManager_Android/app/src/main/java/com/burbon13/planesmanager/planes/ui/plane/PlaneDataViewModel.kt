package com.burbon13.planesmanager.planes.ui.plane

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.data.remote.PlaneDataSource
import com.burbon13.planesmanager.planes.data.PlaneRepository
import com.burbon13.planesmanager.planes.model.Geolocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaneDataViewModel : ViewModel() {
    private val planeRepository = PlaneRepository(PlaneDataSource())

    private val _plane = MutableLiveData<Result<Plane>>()
    val plane: LiveData<Result<Plane>>
        get() = _plane

    private val _planeDeletion = MutableLiveData<Result<Boolean>>()
    val planeDeletion: LiveData<Result<Boolean>>
        get() = _planeDeletion

    private val _planeGeolocation = MutableLiveData<Result<Geolocation>>()
    val planeGeolocation: LiveData<Result<Geolocation>>
        get() = _planeGeolocation

    fun loadPlane(tailNumber: String) {
        Log.d(TAG, "Loading plane with tailNumber=$tailNumber")
        viewModelScope.launch(Dispatchers.IO) {
            _plane.postValue(planeRepository.getPlane(tailNumber))
        }
    }

    fun deletePlane(tailNumber: String) {
        Log.d(TAG, "Deleting plane with tailNumber=$tailNumber")
        viewModelScope.launch(Dispatchers.IO) {
            _planeDeletion.postValue(planeRepository.deletePlane(tailNumber))
        }
    }

    fun updatePlane(newPlane: Plane) {
        Log.d(TAG, "Updating plane=$newPlane")
        viewModelScope.launch(Dispatchers.IO) {
            _plane.postValue(planeRepository.updatePlane(newPlane))
        }
    }

    fun getPlaneGeolocation(tailNumber: String) {
        Log.d(TAG, "Loading geolocation for tailNumber=$tailNumber")
        viewModelScope.launch(Dispatchers.IO) {
            _planeGeolocation.postValue(planeRepository.getPlaneGeolocation(tailNumber))
        }
    }
}
