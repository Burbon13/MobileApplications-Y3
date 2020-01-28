package com.burbon13.template.data

import com.burbon13.template.utils.Api


object MyDataSource {

    interface RetrofitService {

    }

    private val retrofitService: RetrofitService = Api.retrofit.create(
        RetrofitService::class.java
    )

}
