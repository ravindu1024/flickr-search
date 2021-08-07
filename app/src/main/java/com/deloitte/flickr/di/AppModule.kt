package com.deloitte.flickr.di

import android.content.Context
import com.deloitte.data.api.PhotosAPI
import com.deloitte.data.api.RequestInterceptor
import com.deloitte.data.api.RetrofitClient
import com.deloitte.data.models.deserializers.PhotoSearchResponseDeserializer
import com.deloitte.data.models.dto.PhotoSearchResponseDto
import com.deloitte.flickr.R
import com.deloitte.flickr.common.StringResourceProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

/**
 * Handles app level objects that can be shared by multiple usecases and viewmodels
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
class AppModule {

    @Provides
    @ActivityRetainedScoped
    fun provideStringResourceProvider(@ApplicationContext context: Context): StringResourceProvider {
        return object: StringResourceProvider {
            override fun getString(id: Int): String {
                return context.getString(id)
            }
        }
    }

    @Provides
    @ActivityRetainedScoped
    fun provideCustomGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                PhotoSearchResponseDto::class.java,
                PhotoSearchResponseDeserializer(Gson())
            )
            .create()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideRetrofit(@ApplicationContext context: Context, gson: Gson): Retrofit {
        val apiKey = context.getString(R.string.flickr_api_key)
        val apiBaseUrl = context.getString(R.string.flickr_api_baseurl)
        return RetrofitClient.init(apiBaseUrl, RequestInterceptor(apiKey), gson)
    }

    @Provides
    @ActivityRetainedScoped
    fun providePhotosApi(retrofit: Retrofit): PhotosAPI{
        return retrofit.create(PhotosAPI::class.java)
    }
}