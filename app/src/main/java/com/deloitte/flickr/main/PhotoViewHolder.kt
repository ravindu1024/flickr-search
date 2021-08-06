package com.deloitte.flickr.main

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deloitte.data.models.domain.Photo
import com.deloitte.flickr.databinding.RowPhotoCellBinding

class PhotoViewHolder(private val binding: RowPhotoCellBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo, itemClickCallBack: ((Photo) -> Unit)?) {
        Glide.with(binding.root.context)
            .load(photo.url)
            .into(binding.imageView)

        binding.root.tag = photo
        binding.root.setOnClickListener {
            itemClickCallBack?.invoke(it.tag as Photo)
        }
    }
}