package com.example.imagesearch.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.example.imagesearch.R
import com.example.imagesearch.databinding.FragmentListOfPicturesBinding
import com.example.imagesearch.ui.adapters.PhotoLoadStateAdapter
import com.example.imagesearch.ui.adapters.PhotoAdapter
import com.example.imagesearch.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentListOfPictures : Fragment() {
    private var _binding: FragmentListOfPicturesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PhotoViewModel by activityViewModels()
    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListOfPicturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.recyclerView) {
            setHasFixedSize(true)
            itemAnimator = null
            photoAdapter = PhotoAdapter()

            photoAdapter.onItemClickShowDetails = { result ->
                viewModel.resultToShowInFragmentDetails.postValue(result)
                findNavController().navigate(R.id.action_fragmentListOfPictures_to_fragmentDetails)
            }

            adapter = photoAdapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadStateAdapter {
                    photoAdapter.retry()
                },
                footer = PhotoLoadStateAdapter {
                    photoAdapter.retry()
                }
            )
        }

        binding.button.setOnClickListener {
            photoAdapter.retry()
        }

        viewModel.photos.observe(viewLifecycleOwner, { result ->
            photoAdapter.submitData(viewLifecycleOwner.lifecycle, result)
        })

        photoAdapter.addLoadStateListener { combinedLoadStates ->
            binding.apply {
                progressBar.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
                recyclerView.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
                button.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                textViewCouldNotBeLoaded.isVisible =
                    combinedLoadStates.source.refresh is LoadState.Error

                if (combinedLoadStates.source.refresh is LoadState.NotLoading &&
                    combinedLoadStates.append.endOfPaginationReached &&
                    photoAdapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewNoResultsFound.isVisible = true
                } else {
                    textViewNoResultsFound.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        val menuItemSearch = menu.findItem(R.id.menu_item_search)
        val searchView = menuItemSearch.actionView as SearchView
        val searchQuery = viewModel.searchQuery.value

        searchQuery?.let {
            searchView.post {
                menuItemSearch.expandActionView()
                searchView.setQuery(searchQuery, false)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.setQuery(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.postValue(newText)
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}