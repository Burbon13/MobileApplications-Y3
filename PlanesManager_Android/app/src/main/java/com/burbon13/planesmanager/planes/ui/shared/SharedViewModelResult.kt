package com.burbon13.planesmanager.planes.ui.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.burbon13.planesmanager.core.Result


open class SharedViewModelResult<T : Any> : ViewModel() {
    val planeActionResult = MutableLiveData<Result<T>>()
}
