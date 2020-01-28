package com.burbon13.examen.data.model

import com.burbon13.examen.data.model.Product

data class ProductPage(
    val total: Int,
    val page: Int,
    val products: List<Product>
)