package com.yazag.capstoneproject.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.yazag.capstoneproject.R
import com.yazag.capstoneproject.common.gone
import com.yazag.capstoneproject.common.strike
import com.yazag.capstoneproject.common.viewBinding
import com.yazag.capstoneproject.common.visible
import com.yazag.capstoneproject.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)

    private val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()

    private val imagesAdapter = DetailAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductDetail(args.id)

        observeData()

        with(binding){
            rvDetailImages.adapter = imagesAdapter

            ivBack.setOnClickListener{
                findNavController().navigateUp()
            }

            ivFav.setOnClickListener {
                viewModel.setFavoriteState(args.id)
            }

            btnAddCart.setOnClickListener {
                viewModel.addToCart(args.id)
                Snackbar.make(requireView(), "Product added to cart.", 1000).show()
            }
        }
    }

    private fun observeData() {
        with(binding) {
            with(viewModel){
                detailState.observe(viewLifecycleOwner) { state ->
                    when (state) {
                        DetailState.Loading -> {
                            progressBar.visible()
                        }

                        is DetailState.SuccessState -> {
                            progressBar.gone()

                            val imageList = listOf(state.product.imageOne, state.product.imageTwo, state.product.imageThree)
                            imagesAdapter.updateList(imageList)
                            tvTitleDetail.text = state.product.title
                            tvCategory.text = state.product.category
                            tvDescDetail.text = state.product.description
                            tvRateDetail.text = "${state.product.rate}"
                            ((state.product.rate).toFloat()).also { rbDetail.rating = it }

                            if(state.product.saleState) {
                                tvSalePriceDetail.visible()
                                "${state.product.price} ₺".also { tvPriceDetail.text = it }
                                "${state.product.salePrice} ₺".also { tvSalePriceDetail.text = it }
                                tvPriceDetail.strike = true
                            } else if(!state.product.saleState){
                                "${state.product.price} ₺".also { tvPriceDetail.text = it }
                                tvSalePriceDetail.gone()
                            }

                            ivFav.setBackgroundResource(
                                if (state.product.isFav) R.drawable.ic_fav_selected
                                else R.drawable.ic_fav_unselected
                            )
                        }

                        is DetailState.EmptyScreen -> {
                            progressBar.gone()
                            ivEmpty.visible()
                            tvEmpty.visible()
                            tvEmpty.text = state.failMessage
                        }

                        is DetailState.ShowPopUp -> {
                            progressBar.gone()
                            Snackbar.make(requireView(), state.errorMessage, 1000).show()
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }
}