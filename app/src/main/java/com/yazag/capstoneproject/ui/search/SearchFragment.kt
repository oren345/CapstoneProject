package com.yazag.capstoneproject.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yazag.capstoneproject.R
import com.yazag.capstoneproject.common.gone
import com.yazag.capstoneproject.common.viewBinding
import com.yazag.capstoneproject.common.visible
import com.yazag.capstoneproject.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val viewModel by viewModels<SearchViewModel>()

    private val searchAdapter = SearchProductsAdapter(onProductClick = ::onProductClick)
    private var categoryAdapter = CategoryAdapter(onCategoryClick = ::onCategoryClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

        with(binding) {
            rvSearch.adapter = searchAdapter
            rvCategory.adapter = categoryAdapter

            with(viewModel){
                searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let {
                            if(it.length > 3){
                                searchProduct(it)
                            } else {
                                searchAdapter.submitList(emptyList())
                            }
                        }
                        return true
                    }
            }   )}
        }
    }

    private fun observeData() = with(binding) {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Loading -> progressBar.visible()

                is SearchState.SuccessState -> {
                    progressBar.gone()
                    ivEmpty.gone()
                    tvEmpty.gone()
                    rvCategory.gone()
                    rvSearch.visible()
                    searchAdapter.submitList(state.products)
                }

                is SearchState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    rvSearch.gone()
                    tvEmpty.text = state.failMessage
                }

                is SearchState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }

                else -> {}
            }
        }

        viewModel.categoryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CategoryState.Loading -> progressBar.visible()

                is CategoryState.SuccessState -> {
                    progressBar.gone()
                    ivEmpty.gone()
                    tvEmpty.gone()
                    rvSearch.gone()
                    rvCategory.visible()
                    categoryAdapter.submitList(state.products)
                }

                is CategoryState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    rvSearch.gone()
                    tvEmpty.text = state.failMessage
                }

                is CategoryState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }

                else -> {}
            }
        }
    }

    private fun onProductClick(id: Int) {
        findNavController().navigate(SearchFragmentDirections.searchToDetail(id))
    }
    private fun onCategoryClick(id: Int) {
        findNavController().navigate(SearchFragmentDirections.searchToDetail(id))
    }
}