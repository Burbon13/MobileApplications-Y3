package com.burbon13.planesmanager.planes.ui.form

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.data.remote.PlaneDataSource
import com.burbon13.planesmanager.planes.data.PlaneRepository
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.planes.model.PlaneValidator
import kotlinx.coroutines.launch


class PlaneFormViewModel : ViewModel() {
    private val planeRepository = PlaneRepository(PlaneDataSource())
    private val planeValidator = PlaneValidator()

    val updatingPlane = MutableLiveData<Boolean>()

    private val _planeFormState = MutableLiveData<PlaneFormState>()
    val planeFormState: LiveData<PlaneFormState>
        get() = _planeFormState

    private val _addPlaneResult = MutableLiveData<Result<Plane>>()
    val addPlaneResult: MutableLiveData<Result<Plane>>
        get() = _addPlaneResult

    private val _processing = MutableLiveData<Boolean>()
    val processing: LiveData<Boolean>
        get() = _processing

    fun addPlane(
        tailNumber: String,
        brand: Plane.Brand,
        model: String,
        engine: Plane.Engine,
        fabricationYear: String,
        price: String
    ) {
        Log.d(TAG, "Add new plane with tailNumber=$tailNumber")
        viewModelScope.launch {
            _processing.value = true
            _addPlaneResult.value = planeRepository.addPlane(
                Plane(
                    tailNumber,
                    brand,
                    model,
                    fabricationYear.toInt(),
                    engine,
                    price.toLong()
                )
            )
            _processing.value = false
        }
    }

    fun updatePlane() {

    }

    fun planeFormStateChanged(
        tailNumber: String,
        model: String,
        fabricationYear: String,
        price: String
    ) {
        Log.v(TAG, "PlaneFormState changed")
        var validData = true
        var tailError: Int? = null
        var modelError: Int? = null
        var yearError: Int? = null
        var priceError: Int? = null

        if (!planeValidator.isTailNumberValid(tailNumber)) {
            tailError = R.string.invalid_tail_number
            validData = false
        }
        if (!planeValidator.isModelValid(model)) {
            modelError = R.string.invalid_model
            validData = false
        }
        try {
            if (!planeValidator.isFabricationYearValid(fabricationYear.toInt())) {
                yearError = R.string.invalid_fabrication_year
                validData = false
            }
        } catch (e: NumberFormatException) {
            yearError = R.string.invalid_fabrication_year
            validData = false
        }
        try {
            if (!planeValidator.isPriceValid(price.toLong())) {
                priceError = R.string.invalid_price
                validData = false
            }
        } catch (e: java.lang.NumberFormatException) {
            priceError = R.string.invalid_price
            validData = false
        }

        _planeFormState.value =
            PlaneFormState(tailError, modelError, yearError, priceError, validData)
    }
}
