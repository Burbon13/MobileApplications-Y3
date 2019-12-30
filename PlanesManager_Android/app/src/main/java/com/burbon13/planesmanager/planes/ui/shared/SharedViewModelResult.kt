package com.burbon13.planesmanager.planes.ui.shared

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG


open class SharedViewModelResult<T : Any> : ViewModel() {
    private val _addPlaneResult = MutableLiveData<Result<T>>()
    val addPlaneResult: MutableLiveData<Result<T>>
        get() {
            Log.d(TAG, "GET ADD PLANE RESULT !!!LIVEDATA!!!")
            return _addPlaneResult
        }
}
