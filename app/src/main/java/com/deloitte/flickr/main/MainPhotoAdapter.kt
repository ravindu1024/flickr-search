package com.deloitte.flickr.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deloitte.data.models.domain.Photo
import com.deloitte.flickr.databinding.RowLoadingCellBinding
import com.deloitte.flickr.databinding.RowPhotoCellBinding

class MainPhotoAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val TYPE_IMAGE = 0
        const val TYPE_LOADER = 1
    }

    private val layoutInflater = LayoutInflater.from(context)
    private val photoList = ArrayList<Photo>()

    var itemClickCallback: ((Photo) -> Unit)? = null
    var loadMoreCallback: (() -> Unit)? = null

    fun updatePhotos(list: List<Photo>){
        photoList.clear()
        photoList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_IMAGE -> PhotoViewHolder(RowPhotoCellBinding.inflate(layoutInflater))
            else -> LoadingViewHolder(RowLoadingCellBinding.inflate(layoutInflater))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position < photoList.size){
            (holder as PhotoViewHolder).bind(photoList[position], itemClickCallback)
        }else{
            loadMoreCallback?.invoke()
        }
    }

    override fun getItemCount() = if(photoList.isNotEmpty()) photoList.size + 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if(position < photoList.size)
            TYPE_IMAGE
        else
            TYPE_LOADER
    }
}