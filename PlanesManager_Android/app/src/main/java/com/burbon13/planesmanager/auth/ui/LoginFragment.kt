package com.burbon13.planesmanager.auth.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.burbon13.planesmanager.core.Result

import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.TAG
import com.burbon13.planesmanager.core.utils.extensions.afterTextChanged
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        usernameEditText = rootView.findViewById(R.id.username)
        passwordEditText = rootView.findViewById(R.id.password)
        loginButton = rootView.findViewById(R.id.login)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
        setListeners()
    }

    private fun setListeners() {
        viewModel.loginFormState.observe(this, Observer {
            Log.v(TAG, "Login form state modified")
            val loginState = it ?: return@Observer
            login.isEnabled = loginState.isDataValid
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })
        viewModel.loginResult.observe(this, Observer {
            Log.d(TAG, "Login result modified")
            val loginResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (loginResult.succeeded) {
                Log.d(TAG, "Login result is successful, navigation to planes fragment")
                findNavController().navigate(R.id.action_loginActivity_to_planesFragment)
            } else if (loginResult is Result.Error) {
                Log.d(TAG, "Login failed, showing Toast message")
                Toast.makeText(context, loginResult.message, Toast.LENGTH_LONG).show()
            }
        })
        usernameEditText.afterTextChanged {
            Log.v(TAG, "Username login field modified")
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        passwordEditText.afterTextChanged {
            Log.v(TAG, "Password login field modified")
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        loginButton.setOnClickListener {
            Log.v(TAG, "Login button clicked")
            loading.visibility = View.VISIBLE
            viewModel.login(username.text.toString(), password.text.toString())
        }
    }

    // TODO: Delete in the end
    override fun onResume() {
        super.onResume()
        viewModel.login("a@a.com", "aaa")
    }

    override fun onStop() {
        super.onStop()
        username.afterTextChanged {}
        password.afterTextChanged {}
        login.setOnClickListener(null)
    }
}
