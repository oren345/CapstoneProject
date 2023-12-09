package com.yazag.capstoneproject.ui.signin

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yazag.capstoneproject.R
import com.yazag.capstoneproject.common.gone
import com.yazag.capstoneproject.common.viewBinding
import com.yazag.capstoneproject.common.visible
import com.yazag.capstoneproject.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding (FragmentSignInBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnSignIn.setOnClickListener {
                viewModel.signIn(
                    etEmailSignIn.text.toString(),
                    etPasswordSignIn.text.toString()
                )
            }

            tvToSignUp.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.signInToSignUp())
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                requireActivity().finish()
            }
        }

        initObservers()
    }

    private fun initObservers() = with(binding) {
        viewModel.signInState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignInState.Loading -> progressBar.visible()

                is SignInState.GoToHome -> {
                    progressBar.gone()
                    findNavController().navigate(SignInFragmentDirections.signInToHome())
                }

                is SignInState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}