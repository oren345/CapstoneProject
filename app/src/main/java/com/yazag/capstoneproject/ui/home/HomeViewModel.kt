package com.yazag.capstoneproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yazag.capstoneproject.common.Resource
import com.yazag.capstoneproject.data.model.response.ProductUI
import com.yazag.capstoneproject.data.repository.FirebaseAuthenticator
import com.yazag.capstoneproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val firebaseAuthenticator: FirebaseAuthenticator
) : ViewModel() {

    private var _productState = MutableLiveData<ProductState>()
    val productState: LiveData<ProductState> get() = _productState

    private var _saleProductState = MutableLiveData<SaleProductState>()
    val saleProductState: LiveData<SaleProductState> get() = _saleProductState

    fun getProducts() = viewModelScope.launch {
        _productState.value = ProductState.Loading

        _productState.value = when (val result = productRepository.getProducts()) {
            is Resource.Success -> ProductState.SuccessState(result.data)
            is Resource.Fail -> ProductState.EmptyScreen(result.failMessage)
            is Resource.Error -> ProductState.ShowPopUp(result.errorMessage)
        }
    }

    fun getSaleProducts() = viewModelScope.launch {
        _saleProductState.value = SaleProductState.Loading

        _saleProductState.value = when (val result = productRepository.getSaleProducts()) {
            is Resource.Success -> SaleProductState.SuccessState(result.data)
            is Resource.Fail -> SaleProductState.EmptyScreen(result.failMessage)
            is Resource.Error -> SaleProductState.ShowPopUp(result.errorMessage)
        }
    }

    fun setFavoriteState(product: ProductUI) = viewModelScope.launch {
        if (product.isFav) {
            productRepository.deleteFromFavorites(product)
        } else {
            productRepository.addToFavorites(product)
        }
        getProducts()
        getSaleProducts()
    }
    fun signOut() = viewModelScope.launch {
        firebaseAuthenticator.signOut()
        _productState.value = ProductState.GoToSignIn
    }
}

sealed interface ProductState {
    object Loading : ProductState
    object GoToSignIn : ProductState
    data class SuccessState(val products: List<ProductUI>) : ProductState
    data class EmptyScreen(val failMessage: String) : ProductState
    data class ShowPopUp(val errorMessage: String) : ProductState
}


sealed interface SaleProductState {
    object  Loading : SaleProductState
    data class SuccessState(val products: List<ProductUI>) : SaleProductState
    data class EmptyScreen(val failMessage : String) : SaleProductState
    data class ShowPopUp(val errorMessage : String) : SaleProductState
}
