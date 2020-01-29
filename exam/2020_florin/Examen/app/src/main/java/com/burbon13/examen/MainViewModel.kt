package com.burbon13.examen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.examen.data.MyDataSource
import com.burbon13.examen.data.MyRepository
import com.burbon13.examen.data.model.Item
import com.burbon13.examen.data.model.Product
import com.burbon13.examen.utils.MyResult
import com.burbon13.examen.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.function.Predicate
import java.util.stream.Collectors


class MainViewModel : ViewModel() {
    val products = ArrayList<Product>()
    val searchProducts = MutableLiveData<List<Product>>()
    val downloadAvailable = MutableLiveData<Boolean>()
    val uploadAvailable = MutableLiveData<Boolean>()
    val status = MutableLiveData<String>()
    val items = ArrayList<Item>()
    var processing = false

    init {
        paginatedLoad()
    }

    fun wsReceived() {
        if (!processing || downloadAvailable.value == null || downloadAvailable.value == false) {
            downloadAvailable.postValue(true)
        }
    }

    fun downloadAgain() {
        paginatedLoad()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun search(searchText: String) {
        val result =
            products
                .stream()
                .filter { product -> product.name.startsWith(searchText) }
                .limit(5)
                .collect(Collectors.toList())
        Log.d(TAG, "Search result: $result")
        searchProducts.postValue(result)
    }

    private fun paginatedLoad() {
        processing = true
        downloadAvailable.value = false
        uploadAvailable.value = false
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue("Downloading 1 / ...")
            var currentPageIndex = 0
            while (true) {
                val currentPageResult = MyRepository.getProductsPage(currentPageIndex)

                if (currentPageResult.succeeded) {
                    val currentPage = (currentPageResult as MyResult.Success).data
                    if (currentPage.products.isEmpty()) {
                        break
                    }

                    products.addAll(currentPage.products)

                    currentPageIndex += 1
                    if (currentPageIndex <= currentPage.total / 10) {
                        status.postValue("Downloading $currentPageIndex / ${currentPage.total / 10}")
                    }
                } else {
                    // TODO
                }
            }
            status.postValue("Idle")
            processing = false
        }
    }

    fun modifyQuantity(code: Int, quantity: Int) {
        if (items.find { it.code == code } == null) {
            items.add(Item(code, quantity))
        } else {
            items.find { it.code == code }?.quantity = quantity
        }
        uploadAvailable.postValue(true)
    }

    fun uploadItems() {
        processing = true
        downloadAvailable.value = false
        uploadAvailable.value = false
        viewModelScope.launch(Dispatchers.IO) {
            var pos = 0
            items.forEach { item ->
                status.postValue("Uploading ${pos + 1} / ${items.size}")
                pos += 1
                MyRepository.upload(item)
            }
            items.clear()
            status.postValue("Idle")
            processing = false
        }
    }
}