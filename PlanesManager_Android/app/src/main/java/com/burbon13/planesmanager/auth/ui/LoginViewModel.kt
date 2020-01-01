package com.burbon13.planesmanager.auth.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.burbon13.planesmanager.auth.data.LoginRepository
import androidx.lifecycle.viewModelScope

import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.auth.data.TokenHolder
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState>
        get() = _loginFormState

    private val _loginResult = MutableLiveData<Result<TokenHolder>>()
    val loginResult: LiveData<Result<TokenHolder>>
        get() = _loginResult

    fun login(username: String, password: String) {
        Log.d(TAG, "Login username:$username password:$password")
        viewModelScope.launch {
            _loginResult.value = LoginRepository.login(username, password)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        Log.v(TAG, "Login data changed")
        if (!isUserNameValid(username)) {
            _loginFormState.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    fun checkAlreadyLogin() {
        Log.d(TAG, "Checking if user already logged in")
        val existingToken = LoginRepository.userLoggedIn()
        if (existingToken != null) {
            Log.d(TAG, "User already logged in")
            val tokenHolder = TokenHolder(existingToken)
            LoginRepository.setLoggedInUser(tokenHolder)
            _loginResult.value = Result.Success(tokenHolder)
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
