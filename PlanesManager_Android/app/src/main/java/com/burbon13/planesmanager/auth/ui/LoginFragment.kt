package com.burbon13.planesmanager.auth.ui

import android.graphics.drawable.AnimationDrawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Button
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.burbon13.planesmanager.MainViewModel
import com.burbon13.planesmanager.core.Result

import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.core.utils.extensions.afterTextChanged
import com.burbon13.planesmanager.core.utils.ui.Animations
import java.lang.Exception


class LoginFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mainViewModel: MainViewModel

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var backgroundAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        mainViewModel =
            ViewModelProviders.of(activity ?: throw Exception("Unable to retrieve activity!"))
                .get(MainViewModel::class.java)
        loginViewModel.checkAlreadyLogin()
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
        loadingProgressBar = rootView.findViewById(R.id.loading)
        val constraintLayout = rootView.findViewById<ConstraintLayout>(R.id.container)
        backgroundAnimation = constraintLayout.background as AnimationDrawable
        backgroundAnimation.setEnterFadeDuration(4000)
        backgroundAnimation.setExitFadeDuration(4000)

        Animations.attachSpringAnimation(rootView.findViewById(R.id.fling))

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onStart()")
        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            Log.v(TAG, "(Observed) Login form state modified")
            val loginState = it ?: return@Observer
            loginButton.isEnabled = loginState.isDataValid
            if (loginState.usernameError != null) {
                usernameEditText.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                passwordEditText.error = getString(loginState.passwordError)
            }
        })
        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "(Observed) Login result modified")
            val loginResult = it ?: return@Observer
            loadingProgressBar.visibility = View.GONE
            if (loginResult.succeeded) {
                Log.d(TAG, "Login result is successful, navigation to planes fragment")
                mainViewModel.login()
                findNavController(this)
                    .navigate(LoginFragmentDirections.actionLoginActivityToPlanesFragment())
            } else if (loginResult is Result.Error) {
                Log.d(TAG, "Login failed, showing Toast message")
                Toast.makeText(context, loginResult.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        usernameEditText.afterTextChanged {
            Log.v(TAG, "(Observed) Username login field modified")
            loginViewModel.loginDataChanged(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
        passwordEditText.afterTextChanged {
            Log.v(TAG, "(Observed) Password login field modified")
            loginViewModel.loginDataChanged(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
        loginButton.setOnClickListener {
            Log.v(TAG, "(Observed) Login button clicked")
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
        backgroundAnimation.start()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop()")
        usernameEditText.afterTextChanged {}
        passwordEditText.afterTextChanged {}
        loginButton.setOnClickListener(null)
        backgroundAnimation.stop()
    }
}
