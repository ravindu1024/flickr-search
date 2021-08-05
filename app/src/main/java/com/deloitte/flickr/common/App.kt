package com.deloitte.flickr.common

import android.app.Application
import com.deloitte.data.api.PhotosAPI
import com.deloitte.data.api.RequestInterceptor
import com.deloitte.data.api.RetrofitClient
import com.deloitte.data.models.deserializers.PhotoSearchResponseDeserializer
import com.deloitte.data.models.dto.PhotoSearchResponseDto
import com.deloitte.data.usecases.SearchPhotosUseCase
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class App: Application() {

    private lateinit var gson: Gson

    lateinit var getImagesUseCase: SearchPhotosUseCase

    override fun onCreate() {
        super.onCreate()

        initManualDI()
    }

    /**
     * Ideally, I would like to use Dagger or Hilt for this but given the time constraints
     * I'm doing manual DI
     */
    private fun initManualDI(){

        // Custom gson with custom deserializers
        gson = GsonBuilder()
            .registerTypeAdapter(PhotoSearchResponseDto::class.java, PhotoSearchResponseDeserializer(Gson()))
            .create()

        // Networking (Retrofit with custom gson)
        val apiKey = "96358825614a5d3b1a1c3fd87fca2b47"
        val retrofit = RetrofitClient.init("https://api.flickr.com/", RequestInterceptor(apiKey), gson)

        // Use cases
        getImagesUseCase = SearchPhotosUseCase(api = retrofit.create(PhotosAPI::class.java), staticBaseUrl = "static.flickr.com")

    }
}