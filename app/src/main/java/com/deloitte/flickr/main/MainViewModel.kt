package com.deloitte.flickr.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deloitte.data.models.domain.Photo
import com.deloitte.data.usecases.SearchPhotosUseCase
import com.deloitte.flickr.common.ViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel(private val searchUseCase: SearchPhotosUseCase) : ViewModel() {

    val viewState = MutableLiveData<ViewState>()
    val photoList = MutableLiveData<List<Photo>>()

    private val disposable = CompositeDisposable()
    private var pageNum = 1
    private var searchInProgress = false
    private var searchQuery = ""

    fun searchImages(query: String, newSearch: Boolean) {

        if(searchInProgress){
            // Ignore if there is a search in progress
                Log.d("FL", "ignoring search")
            return
        }else{
            searchInProgress = true
        }

        if (newSearch) {
            pageNum = 1
            searchQuery = query
        } else {
            pageNum++
        }

        disposable.add(
            searchUseCase.searchPhotos(searchQuery, pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (newSearch) {
                        viewState.value = ViewState.ShowLoading
                    }
                }
                .subscribe({ photos ->
                    searchInProgress = false
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
                    searchInProgress = false
                    viewState.value = ViewState.ShowError(message = "Could not load images")
                })
        )
    }
}