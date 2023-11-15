package com.yazag.capstoneproject.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yazag.capstoneproject.R
import com.yazag.capstoneproject.common.viewBinding
import com.yazag.capstoneproject.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {
    private val binding by viewBinding (FragmentPaymentBinding::bind)

    private val viewModel by viewModels<PaymentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            btnPay.setOnClickListener {
                viewModel.payment(
                    etName.text.toString(),
                    etCardNumber.text.toString(),
                    etCardMonth.text.toString(),
                    etCardYear.text.toString(),
                    etCvv.text.toString(),
                    etAddress.text.toString()
                )
            }
        }
        observeData()
    }
    private fun observeData() = with(binding) {
        viewModel.paymentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PaymentState.GoToSuccess -> {
                    findNavController().navigate(PaymentFragmentDirections.paymentToPaymentSuccess())
                }

                is PaymentState.ShowPopUp -> {
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
                else -> {}
            }
        }
    }
}