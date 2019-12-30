package com.burbon13.planesmanager.planes.ui.shared

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG


class SharedViewModelResult: ViewModel() {
    val _addPlaneResult = MutableLiveData<Result<String>>()
    val addPlaneResult: MutableLiveData<Result<String>>
    get() {
        Log.d(TAG, "GET ADD PLANE RESULT !!!LIVEDATA!!!")
        return _addPlaneResult
    }
}
