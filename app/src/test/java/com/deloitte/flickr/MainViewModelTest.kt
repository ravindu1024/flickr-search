package com.deloitte.flickr

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deloitte.data.api.ApiException
import com.deloitte.data.models.domain.Photo
import com.deloitte.data.usecases.SearchPhotosUseCase
import com.deloitte.flickr.common.StringResourceProvider
import com.deloitte.flickr.common.ViewState
import com.deloitte.flickr.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @Rule
    @JvmField
    var rx2OverrideRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val searchUseCase = mockk<SearchPhotosUseCase>()
    private val stringProvider = mockk<StringResourceProvider>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup(){
        viewModel = MainViewModel(searchUseCase, stringProvider)
    }

    @Test
    fun `search - new search`(){
        val p1 = Photo("id1", "sec1", "url1", "")
        val p2 = Photo("id2", "sec2", "url2", "")
        every { searchUseCase.searchPhotos(any(), 1) } returns Single.just(listOf(p1, p2))

        viewModel.searchImages("cat", true)
        verify { searchUseCase.searchPhotos("cat", 1) }

        Assert.assertEquals(viewModel.photoList.value!!.size, 2)
        Assert.assertEquals(viewModel.photoList.value!!.first().id, "id1")
        Assert.assertEquals(viewModel.photoList.value!!.first().secret, "sec1")
    }

    @Test
    fun `search - paged search`(){
        val p1 = Photo("id1", "sec1", "url1", "")
        val p2 = Photo("id2", "sec2", "url2", "")
        every { searchUseCase.searchPhotos(any(), any()) } returns Single.just(listOf(p1, p2))

        viewModel.searchImages("cat", true)
        verify { searchUseCase.searchPhotos("cat", 1) }

        viewModel.searchImages("cat", false)
        verify { searchUseCase.searchPhotos("cat", 2) }

        Assert.assertEquals(viewModel.photoList.value!!.size, 4)
    }

    @Test
    fun `search - new subsequent search`(){
        val p1 = Photo("id1", "sec1", "url1", "")
        val p2 = Photo("id2", "sec2", "url2", "")
        every { searchUseCase.searchPhotos(any(), any()) } returns Single.just(listOf(p1, p2))

        viewModel.searchImages("cat", true)
        verify { searchUseCase.searchPhotos("cat", 1) }

        viewModel.searchImages("car", true)
        verify { searchUseCase.searchPhotos("car", 1) }

        Assert.assertEquals(viewModel.photoList.value!!.size, 2)
    }

    @Test
    fun `search - error`(){
        every { searchUseCase.searchPhotos(any(), 1) } returns Single.error(ApiException())
        every { stringProvider.getString(any()) } returns "Could not load images"

        viewModel.searchImages("cat", true)

        val state = viewModel.viewState.value as ViewState.ShowError
        Assert.assertEquals(state.message, "Could not load images")
    }
}
