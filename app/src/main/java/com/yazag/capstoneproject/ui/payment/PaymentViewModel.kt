package com.yazag.capstoneproject.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yazag.capstoneproject.data.repository.FirebaseAuthenticator
import com.yazag.capstoneproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val firebaseAuthenticator: FirebaseAuthenticator,
    private val productRepository: ProductRepository
): ViewModel(){

    private var _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> get() = _paymentState

    fun payment(name: String, number: String, month: String, year: String, cvv: String, address: String) = viewModelScope.launch {

        val errorMessage = when {
            name.isEmpty() -> "Cartholder name cannot be left blank!"
            number.isEmpty() -> "Credit Cart number cannot be left blank!"
            number.length < 16 -> "Credit card number must be 16 digits!"
            month.isEmpty() -> "Month cannot be left blank!"
            month.toInt() > 12 -> "You have logged in incorrectly! Please check again!"
            year.isEmpty() -> "Year cannot be left blank!"
            year.toInt() < 2023 -> "You have logged in invalidly! Please check again!"
            cvv.isEmpty() -> "CVV cannot be left blank!"
            cvv.toInt() < 100 -> "You have logged in incorrectly! Please check again!"
            address.isEmpty() -> "Address cannot be left blank!"
            else -> null
        }

        if(errorMessage != null){
            _paymentState.value = PaymentState.ShowPopUp(errorMessage)
        }else{
            _paymentState.value = PaymentState.GoToSuccess
            productRepository.clearCart(firebaseAuthenticator.getFirebaseUserUid())
        }
    }
}

sealed interface PaymentState{

    data object GoToSuccess : PaymentState
    data class ShowPopUp(val errorMessage: String) : PaymentState
}