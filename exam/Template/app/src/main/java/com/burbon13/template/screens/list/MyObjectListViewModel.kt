package com.burbon13.template.screens.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.burbon13.template.myobjects.data.MyObjectRepository
import com.burbon13.template.myobjects.model.MyObject


class MyObjectListViewModel : ViewModel() {
    private val _planeMutableLiveDate = MyObjectRepository.getAllPlanes()
    val planeLiveData: LiveData<List<MyObject>>
        get() = _planeMutableLiveDate

    private val _toastMessageMutableLiveData = MutableLiveData<String>()
    val toastMessageLiveData: LiveData<String>
        get() = _toastMessageMutableLiveData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading
}