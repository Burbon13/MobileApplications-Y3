package com.burbon13.examen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.examen.data.MyDataSource
import com.burbon13.examen.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    init {
        Log.d(TAG, "Init MainViewModel")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Before test!")
            MyDataSource.test()
            Log.d(TAG, "After test!")
        }
    }
}