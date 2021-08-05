package com.deloitte.flickr.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.deloitte.flickr.R
import com.deloitte.flickr.common.ViewState
import com.deloitte.flickr.databinding.FragmentMainBinding
import com.deloitte.flickr.common.createViewModel
import com.deloitte.flickr.common.factories
import com.deloitte.flickr.common.hideKeyboard
import com.deloitte.flickr.details.DetailFragment
import com.google.android.material.snackbar.Snackbar

class MainFragment: Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private val adapter by lazy { MainPhotoAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        viewModel = createViewModel { MainViewModel(factories().getImagesUseCase) }

        setupUi()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }

    private fun setupUi(){
        binding.photoList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.photoList.adapter = adapter
    }

    private fun bindViews(){
        viewModel.photoList.observe(requireActivity()){ photos ->
            adapter.updatePhotos(photos)
        }

        binding.editSearchTerm.setOnKeyListener { _, keyCode, event ->
            if(event.action == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)){
                hideKeyboard()
                viewModel.searchImages(binding.editSearchTerm.text.toString(), true)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        adapter.loadMoreCallback = {
            viewModel.searchImages(binding.editSearchTerm.text.toString(), false)
        }

        adapter.itemClickCallback = { photo ->
            val bundle = Bundle()
            bundle.putParcelable(DetailFragment.ARG_PHOTO, photo)
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_detailFragment, bundle)
        }

        viewModel.viewState.observe(requireActivity(), { state ->
            when (state) {
                ViewState.ShowLoading -> binding.progressBar.visibility = View.VISIBLE

                ViewState.DismissLoading -> binding.progressBar.visibility = View.GONE

                is ViewState.ShowError -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.swipeLayout, state.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }
}