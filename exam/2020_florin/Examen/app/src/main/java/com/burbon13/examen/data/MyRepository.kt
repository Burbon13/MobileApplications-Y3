package com.burbon13.examen.data

import com.burbon13.examen.data.model.Item
import com.burbon13.examen.data.model.ProductPage
import com.burbon13.examen.utils.MyResult


object MyRepository {
    suspend fun getProductsPage(page: Int): MyResult<ProductPage> {
        return MyDataSource.getPage(page)
    }

    suspend fun upload(item: Item) {
        MyDataSource.uploadItem(item)
    }
}
