package com.yazag.capstoneproject.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yazag.capstoneproject.common.gone
import com.yazag.capstoneproject.common.strike
import com.yazag.capstoneproject.common.visible
import com.yazag.capstoneproject.data.model.response.ProductUI
import com.yazag.capstoneproject.databinding.ItemCartProductBinding

class CartAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : ListAdapter<ProductUI, CartAdapter.CartProductViewHolder>(CartProductViewHolder.CartProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onDeleteClick
        )
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class CartProductViewHolder(
        private val binding: ItemCartProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onDeleteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvCartTitle.text = product.title

                if (product.saleState) {
                    tvCartSalePrice.visible()
                    tvCartPrice.text = "${product.price} ₺"
                    tvCartSalePrice.text = "${product.salePrice} ₺"
                    tvCartPrice.strike = true
                } else {
                    tvCartPrice.text = "${product.price} ₺"
                    tvCartPrice.strike = false
                    tvCartSalePrice.gone()
                }

                    Glide.with(ivCart).load(product.imageOne).into(ivCart)

                    root.setOnClickListener {
                        onProductClick(product.id)
                    }

                    ivDeleteProductCart.setOnClickListener {
                        onDeleteClick(product.id)
                    }

            }
        }

        class CartProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
            override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}