package com.deloitte.flickr.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deloitte.data.models.domain.Photo
import com.deloitte.data.usecases.SearchPhotosUseCase
import com.deloitte.flickr.R
import com.deloitte.flickr.common.StringResourceProvider
import com.deloitte.flickr.common.ViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val searchUseCase: SearchPhotosUseCase,
    private val stringProvider: StringResourceProvider
) : ViewModel() {

    val viewState = MutableLiveData<ViewState>()
    val photoList = MutableLiveData<List<Photo>>()

    private val disposable = CompositeDisposable()
    private var pageNum = 1
    private var searchQuery = ""

    fun searchImages(query: String, newSearch: Boolean) {

        if (viewState.value is ViewState.InProgress) {
            // Ignore if there is a search in progress
            return
        }

        if (newSearch) {
            pageNum = 1
            // saving the query for subsequent pages even if the search field is cleared
            searchQuery = query
        } else {
            pageNum++
        }

        disposable.add(
            searchUseCase.searchPhotos(searchQuery, pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.value = ViewState.InProgress(showLoader = newSearch)
                }
                .subscribe({ photos ->
                    viewState.value = ViewState.DismissLoading

                    if (newSearch) {
                        photoList.value = photos
                    } else {
                        val newList = mutableListOf<Photo>()
                        photoList.value?.let { newList.addAll(it) }
                        newList.addAll(photos)

                        photoList.value = newList
                    }
                }, {
                    viewState.value =
                        ViewState.ShowError(message = stringProvider.getString(R.string.error_image_search))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}