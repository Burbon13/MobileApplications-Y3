package com.burbon13.planesmanager.auth.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.burbon13.planesmanager.auth.data.LoginRepository
import androidx.lifecycle.viewModelScope

import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.auth.data.LoginDataSource
import com.burbon13.planesmanager.auth.data.TokenHolder
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository(LoginDataSource())

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Result<TokenHolder>>()
    val loginResult: LiveData<Result<TokenHolder>> = _loginResult

    fun login(username: String, password: String) {
        Log.d(TAG, "Login username:$username password:$password")
        viewModelScope.launch {
            _loginResult.value = loginRepository.login(username, password)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        Log.v(TAG, "Login data changed")
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 2
    }
}
