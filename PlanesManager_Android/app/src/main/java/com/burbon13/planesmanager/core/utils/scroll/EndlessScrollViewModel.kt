package com.burbon13.planesmanager.core.utils.scroll

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class EndlessScrollViewModel(endlessScrollState: EndlessScrollState) : ViewModel() {
    val state = MutableLiveData<EndlessScrollState>()

    init {
        state.value = endlessScrollState
    }
}
