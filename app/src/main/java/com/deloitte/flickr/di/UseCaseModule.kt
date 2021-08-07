package com.deloitte.flickr.di

import android.content.Context
import com.deloitte.data.api.PhotosAPI
import com.deloitte.data.usecases.SearchPhotosUseCase
import com.deloitte.flickr.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * This class handles viewmodel scoped providers that create Usecases for viewmodels
 */
@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideSearchPhotosUseCase(@ApplicationContext context: Context, photosAPI: PhotosAPI): SearchPhotosUseCase{
        val staticDomain = context.getString(R.string.flickr_static_domain)
        return SearchPhotosUseCase(
            api = photosAPI,
            staticBaseUrl = staticDomain
        )
    }
}