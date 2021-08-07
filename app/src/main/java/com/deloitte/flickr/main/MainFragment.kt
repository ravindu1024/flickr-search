package com.deloitte.flickr.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.deloitte.flickr.common.*
import com.deloitte.flickr.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), StringResourceProvider {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { MainPhotoAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        setupUi()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }

    private fun setupUi() {
        binding.photoList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.photoList.adapter = adapter
    }

    private fun bindViews() {

        // Photo results
        viewModel.photoList.observe(requireActivity()) { photos ->
            adapter.updatePhotos(photos)
        }

        // Search and Paging
        binding.editSearchTerm.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)) {
                hideKeyboard()
                viewModel.searchImages(binding.editSearchTerm.text.toString(), true)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        adapter.loadMoreCallback = {
            viewModel.searchImages(binding.editSearchTerm.text.toString(), false)
        }

        // Item click handling
        adapter.itemClickCallback = { photo ->
            navigateToDetail(photo)
        }

        // View states
        viewModel.viewState.observe(requireActivity(), { state ->
            when (state) {
                is ViewState.InProgress -> binding.progressBar.visibility =
                    if (state.showLoader) View.VISIBLE else View.GONE

                ViewState.DismissLoading -> binding.progressBar.visibility = View.GONE

                is ViewState.ShowError -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.swipeLayout, state.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }
}