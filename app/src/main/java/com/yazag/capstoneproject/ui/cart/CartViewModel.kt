package com.yazag.capstoneproject.ui.cart

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
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val firebaseAuthenticator: FirebaseAuthenticator
) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState> get() = _cartState

    private val _totalAmount = MutableLiveData(0.0)
    val totalAmount: LiveData<Double> = _totalAmount

    fun getCartProducts() = viewModelScope.launch {
        _cartState.value = CartState.Loading

        when(val result = productRepository.getCartProducts(firebaseAuthenticator.getFirebaseUserUid())) {
            is Resource.Success -> {
                _cartState.value = CartState.SuccessState(result.data)
                _totalAmount.value = result.data.sumOf { product ->
                    if (product.saleState) {
                        product.salePrice
                    } else {
                        product.price
                    }
                }
            }
            is Resource.Error -> {
                _cartState.value = CartState.ShowPopUp(result.errorMessage)
            }
            is Resource.Fail -> {
                _cartState.value = CartState.EmptyScreen(result.failMessage)
            }
        }
    }

    fun deleteFromCart(productId : Int) = viewModelScope.launch {
        productRepository.deleteFromCart(firebaseAuthenticator.getFirebaseUserUid(), productId)
        getCartProducts()
    }

    fun clearCart() = viewModelScope.launch {
        productRepository.clearCart(firebaseAuthenticator.getFirebaseUserUid())
        getCartProducts()
    }

    fun resetTotalAmount() {
        _totalAmount.value = 0.0
    }
}
sealed interface CartState {
    object Loading : CartState
    data class SuccessState(val products: List<ProductUI>) : CartState
    data class EmptyScreen(val failMessage: String) : CartState
    data class ShowPopUp(val errorMessage: String) : CartState
}