package com.burbon13.examen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.examen.data.MyDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            MyDataSource.x()
        }
    }
}