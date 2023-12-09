package com.yazag.capstoneproject.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yazag.capstoneproject.common.Resource
import com.yazag.capstoneproject.data.model.response.Category
import com.yazag.capstoneproject.data.model.response.ProductUI
import com.yazag.capstoneproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> get() = _searchState

    private var _categoryState = MutableLiveData<CategoryState>()
    val categoryState: LiveData<CategoryState> get() = _categoryState

    fun searchProduct(query : String) = viewModelScope.launch {
        _searchState.value = SearchState.Loading

        _searchState.value = when (val result = productRepository.searchProduct(query)) {
            is Resource.Success -> SearchState.SuccessState(result.data)
            is Resource.Fail -> SearchState.EmptyScreen(result.failMessage)
            is Resource.Error -> SearchState.ShowPopUp(result.errorMessage)
        }
    }

    fun categoryItem() = viewModelScope.launch {
        _categoryState.value = CategoryState.Loading

        _categoryState.value = when (val result = productRepository.getCategories()) {
            is Resource.Success -> CategoryState.SuccessState(result.data)
            is Resource.Fail -> CategoryState.EmptyScreen(result.failMessage)
            is Resource.Error -> CategoryState.ShowPopUp(result.errorMessage)
        }
    }
}

sealed interface SearchState {
    data object Loading : SearchState
    data class SuccessState(val products: List<ProductUI>) : SearchState
    data class EmptyScreen(val failMessage: String) : SearchState
    data class ShowPopUp(val errorMessage: String) : SearchState
}

sealed interface CategoryState {
    data object Loading : CategoryState
    data class SuccessState(val products: List<Category>) : CategoryState
    data class EmptyScreen(val failMessage: String) : CategoryState
    data class ShowPopUp(val errorMessage: String) : CategoryState
}
