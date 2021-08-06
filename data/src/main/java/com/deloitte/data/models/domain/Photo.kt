package com.deloitte.data.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val secret: String,
    val url: String,
    val title: String
) : Parcelable