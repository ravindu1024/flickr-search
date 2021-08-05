package com.deloitte.data.models.dto

data class PhotoSearchResponseDto(
    val photos: List<PhotoDto>
)

data class PhotoDto(
    val id: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String
)

