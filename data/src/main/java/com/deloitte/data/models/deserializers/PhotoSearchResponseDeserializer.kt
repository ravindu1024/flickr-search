package com.deloitte.data.models.deserializers

import com.deloitte.data.api.ApiException
import com.deloitte.data.models.dto.PhotoSearchResponseDto
import com.deloitte.data.models.dto.PhotoDto
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class PhotoSearchResponseDeserializer(private val genericGson: Gson): JsonDeserializer<PhotoSearchResponseDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PhotoSearchResponseDto {

        val errorStatus = json?.asJsonObject
            ?.get("stat")?.asString

        if(errorStatus == "fail")
            throw ApiException()


        val photosElement = json?.asJsonObject
            ?.getAsJsonObject("photos")
            ?.getAsJsonArray("photo")

        val photos = genericGson.fromJson(photosElement?.toString() ?: "[]", Array<PhotoDto>::class.java)
        return PhotoSearchResponseDto(photos = photos.toList())
    }
}