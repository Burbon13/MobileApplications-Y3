package com.burbon13.planesmanager.auth.ui

import com.burbon13.planesmanager.auth.ui.LoggedInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
