package com.deloitte.data.models.mappings

import com.deloitte.data.models.domain.Photo
import com.deloitte.data.models.dto.PhotoSearchResponseDto

fun PhotoSearchResponseDto.toDomain(baseUrl: String): List<Photo> {

    return this.photos.map {
        return@map Photo(
            id = it.id,
            secret = it.secret,
            title = it.title,
            url = "https://farm${it.farm}.${baseUrl}/${it.server}/${it.id}_${it.secret}.jpg"
        )
    }
}