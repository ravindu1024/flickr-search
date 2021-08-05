package com.deloitte.data.api

import com.deloitte.data.models.dto.PhotoSearchResponseDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosAPI {

    @GET("services/rest")
    fun searchImages(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
        @Query("method") method: String = "flickr.photos.search",
        @Query("format") format: String = "json",
        @Query("api_key") apiKey: String = ""
    ): Single<PhotoSearchResponseDto>
}