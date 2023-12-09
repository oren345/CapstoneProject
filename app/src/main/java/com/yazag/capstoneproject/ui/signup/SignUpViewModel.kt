package com.yazag.capstoneproject.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yazag.capstoneproject.common.Resource
import com.yazag.capstoneproject.data.repository.FirebaseAuthenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SignUpViewModel @Inject constructor(private val firebaseAuthenticator: FirebaseAuthenticator) : ViewModel() {

    private var _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> get() = _signUpState

    fun signUp(email: String, password : String) = viewModelScope.launch {
        if (checkFields(email, password)) {
            _signUpState.value = SignUpState.Loading

            _signUpState.value = when (val result = firebaseAuthenticator.signUp(email, password)) {
                is Resource.Success -> SignUpState.GoToHome
                is Resource.Fail -> SignUpState.ShowPopUp(result.failMessage)
                is Resource.Error -> SignUpState.ShowPopUp(result.errorMessage)
            }
        }

    }

    private fun checkFields(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _signUpState.value = SignUpState.ShowPopUp("Email cannot be left blank!")
                false
            }

            password.isEmpty() -> {
                _signUpState.value = SignUpState.ShowPopUp("Password cannot be left blank!")
                false
            }

            password.length < 6 -> {
                _signUpState.value = SignUpState.ShowPopUp("Password should be 6 characters at least!")
                false
            }

            else -> true
        }
    }

}
sealed interface SignUpState {
    data object Loading : SignUpState
    data object GoToHome: SignUpState
    data class ShowPopUp(val errorMessage: String) : SignUpState
}