package com.burbon13.planesmanager.planes.ui.plane

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.planes.data.PlaneDataSource
import com.burbon13.planesmanager.planes.data.PlaneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaneDataViewModel : ViewModel() {
    private val planeRepository = PlaneRepository(PlaneDataSource())

    private val _plane = MutableLiveData<Result<Plane>>()
    val plane: LiveData<Result<Plane>>
        get() = _plane


    fun loadPlane(tailNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _plane.postValue(planeRepository.getPlane(tailNumber))
        }
    }
}
