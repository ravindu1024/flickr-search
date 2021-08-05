package com.deloitte.flickr.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.deloitte.data.models.domain.Photo
import com.deloitte.flickr.databinding.FragmentDetailBinding

class DetailFragment: Fragment() {
    companion object{
        const val ARG_PHOTO = "ARG_PHOTO"
    }

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)

        initUi()

        return binding.root
    }

    private fun initUi(){
        val photo = arguments?.getParcelable(ARG_PHOTO) as? Photo
        photo?.let {
            binding.textTitle.text = it.title
            Glide.with(requireContext())
                .load(it.url)
                .into(binding.imageView)
        }
    }
}