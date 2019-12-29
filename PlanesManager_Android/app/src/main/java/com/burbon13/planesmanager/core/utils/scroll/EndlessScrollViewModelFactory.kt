package com.burbon13.planesmanager.core.utils.scroll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


@Suppress("UNCHECKED_CAST")
class EndlessScrollViewModelFactory(private val endlessScrollState: EndlessScrollState) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EndlessScrollViewModel::class.java)) {
            return EndlessScrollViewModel(endlessScrollState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
