package com.deloitte.flickr.common

import android.app.Application
import com.deloitte.data.api.PhotosAPI
import com.deloitte.data.api.RequestInterceptor
import com.deloitte.data.api.RetrofitClient
import com.deloitte.data.models.deserializers.PhotoSearchResponseDeserializer
import com.deloitte.data.models.dto.PhotoSearchResponseDto
import com.deloitte.data.usecases.SearchPhotosUseCase
import com.deloitte.flickr.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()