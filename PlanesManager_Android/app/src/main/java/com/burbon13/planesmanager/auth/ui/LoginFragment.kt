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
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.TAG
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated()")
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        setupLoginForm()
    }

    private fun setupLoginForm() {
        Log.d(TAG, "Setting up login form")
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
            } else if (loginResult.failed) {
                Log.d(TAG, "Login failed, showing Toast message")
                Toast.makeText(context, loginResult.toString(), Toast.LENGTH_LONG).show()
            }
        })
        username.afterTextChanged {
            Log.v(TAG, "Username login field modified")
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        password.afterTextChanged {
            Log.v(TAG, "Password login field modified")
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        login.setOnClickListener {
            Log.v(TAG, "Login button clicked")
            loading.visibility = View.VISIBLE
            viewModel.login(username.text.toString(), password.text.toString())
        }
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
