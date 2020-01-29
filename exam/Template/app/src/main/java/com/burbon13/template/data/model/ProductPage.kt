package com.burbon13.template.data.model

data class ProductPage(
    val total: Int,
    val page: Int,
    val products: List<Product>
)