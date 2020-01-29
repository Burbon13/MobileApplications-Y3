package com.burbon13.examen

import android.content.Context
import android.widget.ArrayAdapter
import com.burbon13.examen.data.model.Product


class ProductsAdapter(
    val myContext: Context,
    val resource: Int,
    val items: List<Product>) :
    ArrayAdapter<Product>(myContext, resource, items) {


}
