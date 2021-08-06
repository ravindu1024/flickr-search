package com.deloitte.flickr.common

import androidx.annotation.StringRes

interface StringResourceProvider {
    fun getString(@StringRes id: Int): String
}