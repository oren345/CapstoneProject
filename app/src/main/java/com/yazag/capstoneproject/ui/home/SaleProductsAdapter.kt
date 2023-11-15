package com.yazag.capstoneproject.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yazag.capstoneproject.R
import com.yazag.capstoneproject.common.strike
import com.yazag.capstoneproject.data.model.response.ProductUI
import com.yazag.capstoneproject.databinding.ItemSaleProductBinding

class SaleProductsAdapter (
    private val onProductClick: (Int) -> Unit,
    private val onFavClick: (ProductUI) -> Unit
) : ListAdapter<ProductUI, SaleProductsAdapter.SaleProductViewHolder>(SaleProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleProductViewHolder {
        return SaleProductViewHolder(
            ItemSaleProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onFavClick
        )
    }

    override fun onBindViewHolder(holder: SaleProductViewHolder, position: Int) = holder.bind(getItem(position))

    class SaleProductViewHolder(
        private val binding: ItemSaleProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onFavClick: (ProductUI) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvSaleProductTitle.text = product.title
                tvSaleProductPrice.text = "${product.price} ₺"
                tvSaleProductSalePrice.text = "${product.salePrice} ₺"
                tvSaleProductPrice.strike = true

                ivSaleProductFavorite.setBackgroundResource(
                    if (product.isFav) R.drawable.ic_fav_selected
                    else R.drawable.ic_fav_unselected
                )

                Glide.with(ivSaleProduct).load(product.imageOne).into(ivSaleProduct)

                root.setOnClickListener {
                    onProductClick(product.id)
                }

                ivSaleProductFavorite.setOnClickListener {
                    onFavClick(product)
                }
            }
        }
    }

    class SaleProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}