package com.yazag.capstoneproject.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yazag.capstoneproject.common.Resource
import com.yazag.capstoneproject.data.model.response.ProductUI
import com.yazag.capstoneproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private var _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    fun getFavorites() = viewModelScope.launch {
        _favoritesState.value = FavoritesState.Loading

        _favoritesState.value = when (val result = productRepository.getFavorites()) {
            is Resource.Success -> FavoritesState.SuccessState(result.data)
            is Resource.Fail -> FavoritesState.EmptyScreen(result.failMessage)
            is Resource.Error -> FavoritesState.ShowPopUp(result.errorMessage)
        }
    }

    fun deleteFromFavorites(product: ProductUI) = viewModelScope.launch {
        productRepository.deleteFromFavorites(product)
        getFavorites()
    }

    fun clearFavorites() = viewModelScope.launch {
        productRepository.clearFavorites()
        getFavorites()
    }
}

sealed interface FavoritesState {
    data object Loading : FavoritesState
    data class SuccessState(val products: List<ProductUI>) : FavoritesState
    data class EmptyScreen(val failMessage: String) : FavoritesState
    data class ShowPopUp(val errorMessage: String) : FavoritesState
}