package com.yazag.capstoneproject.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yazag.capstoneproject.data.model.response.ProductUI
import com.yazag.capstoneproject.databinding.ItemFavProductBinding

class FavoritesAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onDeleteClick: (ProductUI) -> Unit
) : ListAdapter<ProductUI, FavoritesAdapter.FavProductViewHolder>(FavProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavProductViewHolder {
        return FavProductViewHolder(
            ItemFavProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onDeleteClick
        )
    }

    override fun onBindViewHolder(holder: FavProductViewHolder, position: Int) = holder.bind(getItem(position))

    class FavProductViewHolder(
        private val binding: ItemFavProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onDeleteClick: (ProductUI) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title
                rbFav.rating = ((product.rate)?.toFloat() ?:1) as Float

                if(!product.saleState) {
                    tvPrice.text = "${product.price} ₺"
                } else {
                    tvPrice.text = "${product.salePrice} ₺"
                }

                Glide.with(ivProduct).load(product.imageOne).into(ivProduct)

                root.setOnClickListener {
                    onProductClick(product.id)
                }

                ivDelete.setOnClickListener {
                    onDeleteClick(product)
                }
            }
        }
    }

    class FavProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}