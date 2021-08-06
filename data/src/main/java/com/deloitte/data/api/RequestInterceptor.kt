package com.deloitte.data.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the api key as a query param to api calls if it has the apiKey param added. Also adds
 * a custom key to prevent the flickr api from returning a wrapper around the json response
 *
 * This can be easily modified to handle multiple api keys or keys for different sources
 */
class RequestInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val urlBuilder = request.url.newBuilder()

        // Adding this to get Flickr to remove the "jsonFlickrApi" wrapper around json responses
        if (request.method == "GET") {
            urlBuilder.addQueryParameter("nojsoncallback", "1")
        }

        // Adding the api key if required
        if (request.url.queryParameter("api_key") != null) {
            urlBuilder.setQueryParameter("api_key", apiKey).build()
        }

        val authRequest = request
            .newBuilder()
            .url(urlBuilder.build())
            .build()

        return chain.proceed(authRequest)
    }
}