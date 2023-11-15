package com.yazag.capstoneproject.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yazag.capstoneproject.R
import com.yazag.capstoneproject.common.gone
import com.yazag.capstoneproject.common.viewBinding
import com.yazag.capstoneproject.common.visible
import com.yazag.capstoneproject.databinding.FragmentCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val binding by viewBinding(FragmentCartBinding::bind)

    private val viewModel by viewModels<CartViewModel>()

    private val cartAdapter = CartAdapter(onProductClick = ::onProductClick, onDeleteClick = ::onDeleteClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvCartProducts.adapter = cartAdapter
            viewModel.getCartProducts()

            btnToPayment.setOnClickListener {
                //veri varsa kontrolü yap, yoksa snackbar göster
                findNavController().navigate(CartFragmentDirections.cartToPayment())
            }

            btnClearAll.setOnClickListener {
                with(viewModel){
                    clearCart()
                    resetTotalAmount()
                }
            }
        }
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CartState.Loading -> {
                    progressBar.visible()
                    btnClearAll.gone()
                    btnToPayment.gone()
                    tvTotalPrice.gone()
                    tvTotalPriceTitle.gone()
                }

                is CartState.SuccessState -> {
                    progressBar.gone()
                    cartAdapter.submitList(state.products)
                    btnClearAll.visible()
                    btnToPayment.visible()
                    tvTotalPrice.visible()
                    tvTotalPriceTitle.visible()
                }

                is CartState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    rvCartProducts.gone()
                    tvEmpty.text = state.failMessage
                }

                is CartState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
        viewModel.totalAmount.observe(viewLifecycleOwner) { amount->
            tvTotalPrice.text = String.format("%.3f ₺", amount)
        }
    }

    private fun onProductClick(id: Int) {
        findNavController().navigate(CartFragmentDirections.cartToDetail(id))
    }

    private fun onDeleteClick(productId : Int) {
        viewModel.deleteFromCart(productId)
    }
}