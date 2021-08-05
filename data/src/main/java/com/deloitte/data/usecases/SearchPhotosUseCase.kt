package com.deloitte.data.usecases

import com.deloitte.data.api.PhotosAPI
import com.deloitte.data.models.domain.Photo
import com.deloitte.data.models.mappings.toDomain
import io.reactivex.Single

class SearchPhotosUseCase(private val api: PhotosAPI, private val staticBaseUrl: String) {

    fun searchPhotos(query: String, pageNum: Int): Single<List<Photo>>{
        return api.searchImages(query, page = pageNum, pageSize = 25)
            .map {
                it.toDomain(staticBaseUrl)
            }
    }
}