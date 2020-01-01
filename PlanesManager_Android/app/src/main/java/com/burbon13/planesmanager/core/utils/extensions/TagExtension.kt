package com.burbon13.planesmanager.core.utils.extensions


/**
 * Extension function to simplify setting the TAG for logs.
 */
val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }
