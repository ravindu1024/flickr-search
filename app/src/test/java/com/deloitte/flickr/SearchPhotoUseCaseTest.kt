package com.deloitte.flickr

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deloitte.data.api.PhotosAPI
import com.deloitte.data.models.dto.PhotoDto
import com.deloitte.data.models.dto.PhotoSearchResponseDto
import com.deloitte.data.usecases.SearchPhotosUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class SearchPhotoUseCaseTest {

    @Rule
    @JvmField
    var rx2OverrideRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val api = mockk<PhotosAPI>()
    private lateinit var useCase: SearchPhotosUseCase

    @Before
    fun setup(){
        useCase = SearchPhotosUseCase(api, "static.flickr.com")
    }

    @Test
    fun `search - mapping`(){
        val photoDto = PhotoDto("123", "456", "625", "12", "BMW M3")
        val response = PhotoSearchResponseDto(listOf(photoDto))
        every { api.searchImages(any(), any(), any(), any(), any()) } returns Single.just(response)

        val t = useCase.searchPhotos("car", 1).test()
        t.await(5, TimeUnit.SECONDS)

        val photos = t.values().first()
        Assert.assertEquals(photos.first().id, "123")
        Assert.assertEquals(photos.first().secret, "456")
        Assert.assertEquals(photos.first().url, "https://farm12.static.flickr.com/625/123_456.jpg")
    }
}