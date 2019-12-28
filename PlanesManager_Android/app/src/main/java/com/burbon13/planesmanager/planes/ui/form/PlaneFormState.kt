package com.burbon13.planesmanager.planes.ui.form

data class PlaneFormState(
    val tailNumberError: Int? = null,
    val modelError: Int? = null,
    val fabricationYearError: Int? = null,
    val priceError: Int? = null,
    val isDataValid: Boolean = false
)
