package com.burbon13.examen.data.model


data class ProductPage(
    val total: Int,
    val page: Int,
    val products: List<Product>
)
