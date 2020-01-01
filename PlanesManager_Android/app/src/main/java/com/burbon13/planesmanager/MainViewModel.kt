package com.burbon13.planesmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.burbon13.planesmanager.auth.data.LoginRepository


class MainViewModel : ViewModel() {
    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn

    init {
        _loggedIn.value = LoginRepository.userLoggedIn() != null
    }

    fun login() {
        _loggedIn.value = true
    }

    fun logout() {
        _loggedIn.value = false
    }
}
