package com.yazag.capstoneproject.ui.paymentsuccess

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yazag.capstoneproject.R
import com.yazag.capstoneproject.common.viewBinding
import com.yazag.capstoneproject.databinding.FragmentPaymentSuccessBinding

class PaymentSuccessFragment : Fragment(R.layout.fragment_payment_success) {
    private val binding by viewBinding(FragmentPaymentSuccessBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnGoToHome.setOnClickListener {
                findNavController().navigate(PaymentSuccessFragmentDirections.paymentSuccessToHome())
            }
        }
    }

}