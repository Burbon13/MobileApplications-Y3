package com.burbon13.planesmanager.planes.ui.form

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.data.PlaneDataSource
import com.burbon13.planesmanager.planes.data.PlaneRepository
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.core.Result
import kotlinx.coroutines.launch
import java.util.*


class PlaneFormViewModel : ViewModel() {
    private val planeRepository = PlaneRepository(PlaneDataSource())

    val updatingPlane = MutableLiveData<Boolean>()

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

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

        if (!isTailNumberValid(tailNumber)) {
            tailError = R.string.invalid_tail_number
            validData = false
        }
        if (!isModelValid(model)) {
            modelError = R.string.invalid_model
            validData = false
        }
        try {
            if (!isFabricationYearValid(fabricationYear.toInt())) {
                yearError = R.string.invalid_fabrication_year
                validData = false
            }
        } catch (e: NumberFormatException) {
            yearError = R.string.invalid_fabrication_year
            validData = false
        }
        try {
            if (!isPriceValid(price.toLong())) {
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

    private fun isTailNumberValid(tailNumber: String): Boolean {
        return tailNumber.length > 4
    }

    private fun isModelValid(model: String): Boolean {
        return model.length > 2
    }

    private fun isFabricationYearValid(fabricationYear: Int): Boolean {
        return fabricationYear > 1950 && fabricationYear < Calendar.getInstance().get(Calendar.YEAR)
    }

    private fun isPriceValid(price: Long): Boolean {
        return price > 0
    }
}
