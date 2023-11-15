package com.yazag.capstoneproject.ui.detail

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
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val firebaseAuthenticator: FirebaseAuthenticator
) : ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState> get() = _detailState

    fun getProductDetail(id: Int) = viewModelScope.launch {
        _detailState.value = DetailState.Loading

        _detailState.value = when (val result = productRepository.getProductDetail(id)) {
            is Resource.Success -> DetailState.SuccessState(result.data)
            is Resource.Fail -> DetailState.EmptyScreen(result.failMessage)
            is Resource.Error -> DetailState.ShowPopUp(result.errorMessage)
        }
    }
    private val detailData: ProductUI? = null

    fun setFavoriteState(id: Int) = viewModelScope.launch {
        detailData?.let {
            if (it.isFav) {
                productRepository.deleteFromFavorites(it)
            } else {
                productRepository.addToFavorites(it)
            }
            productRepository.getProductDetail(id)
        }
    }

    fun addToCart(productId: Int) = viewModelScope.launch {
        productRepository.addToCart(firebaseAuthenticator.getFirebaseUserUid(), productId)
    }
}

sealed interface DetailState {
    object Loading : DetailState
    data class SuccessState(val product: ProductUI) : DetailState
    data class EmptyScreen(val failMessage: String) : DetailState
    data class ShowPopUp(val errorMessage: String) : DetailState
}