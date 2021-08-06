package com.deloitte.flickr.common

import android.os.Bundle
import androidx.navigation.Navigation
import com.deloitte.data.models.domain.Photo
import com.deloitte.flickr.R
import com.deloitte.flickr.details.DetailFragment
import com.deloitte.flickr.main.MainFragment

fun MainFragment.navigateToDetail(photo: Photo) {
    val bundle = Bundle()
    bundle.putParcelable(DetailFragment.ARG_PHOTO, photo)
    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        .navigate(R.id.action_mainFragment_to_detailFragment, bundle)
}