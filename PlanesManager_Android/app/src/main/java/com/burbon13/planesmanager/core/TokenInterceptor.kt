package com.burbon13.planesmanager.core

import android.util.Log
import com.burbon13.planesmanager.core.utils.extensions.TAG
import okhttp3.Interceptor
import okhttp3.Response


class TokenInterceptor : Interceptor {
    companion object {
        private const val AUTH_HEADER_NAME = "Authorization"
    }

    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "Intercepting request chain: $chain")
        val original = chain.request()
        val originalUrl = original.url()
        if (token == null) {
            Log.d(TAG, "No existing token, proceeding with the request chain")
            return chain.proceed(original)
        }
        Log.d(TAG, "Exiting token, adding Authorization header")
        val requestBuilder = original.newBuilder()
            .addHeader(AUTH_HEADER_NAME, "Bearer $token")
            .url(originalUrl)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
