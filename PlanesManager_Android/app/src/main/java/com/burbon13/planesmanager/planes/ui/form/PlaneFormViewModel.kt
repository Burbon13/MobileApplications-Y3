package com.burbon13.planesmanager.planes.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaneFormViewModel : ViewModel() {

    val updatingPlane = MutableLiveData<Boolean>()

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage


    fun addPlane() {

    }

    fun updatePlane() {

    }
}