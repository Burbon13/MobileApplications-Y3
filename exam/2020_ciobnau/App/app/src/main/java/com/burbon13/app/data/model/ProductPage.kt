package com.burbon13.app.data.model

data class ProductPage(
    val total: Int,
    val page: Int,
    val products: List<Product>
)