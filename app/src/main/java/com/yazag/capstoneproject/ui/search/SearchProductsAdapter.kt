package com.yazag.capstoneproject.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yazag.capstoneproject.common.gone
import com.yazag.capstoneproject.common.strike
import com.yazag.capstoneproject.data.model.response.ProductUI
import com.yazag.capstoneproject.databinding.ItemSearchProductBinding

class SearchProductsAdapter(
    private val onProductClick: (Int) -> Unit
) : ListAdapter<ProductUI, SearchProductsAdapter.SearchProductsViewHolder>(SearchProductsDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductsViewHolder {
        return SearchProductsViewHolder(
            ItemSearchProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick
        )
    }

    override fun onBindViewHolder(holder: SearchProductsViewHolder, position: Int) = holder.bind(getItem(position))

    class SearchProductsViewHolder(
        private val binding: ItemSearchProductBinding,
        private val onProductClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvSearchTitle.text = product.title
                tvSearchCategory.text = product.category

                if(!product.saleState) {
                    "${product.price} ₺".also { tvSearchPrice.text = it }
                    tvSearchSalePrice.gone()
                } else {
                    "${product.price} ₺".also { tvSearchPrice.text = it }
                    "${product.salePrice} ₺".also { tvSearchSalePrice.text = it }
                    tvSearchPrice.strike = true
                }

                Glide.with(ivSearch).load(product.imageOne).into(ivSearch)

                root.setOnClickListener {
                    onProductClick(product.id)
                }
            }
        }
    }

    class SearchProductsDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}