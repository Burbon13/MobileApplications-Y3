package com.burbon13.template.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class MyObjectListViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyObjectListViewModel::class.java)) {
            return MyObjectListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
