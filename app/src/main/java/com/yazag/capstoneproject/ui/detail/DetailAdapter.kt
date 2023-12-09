package com.yazag.capstoneproject.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yazag.capstoneproject.databinding.ItemDetailImagesBinding

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.ProductsViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder =
        ProductsViewHolder(
            ItemDetailImagesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.bind(list[position])

    inner class ProductsViewHolder(private var binding: ItemDetailImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding){
                Glide.with(ivDetail).load(item).into(ivDetail)
            }
        }
    }

    override fun getItemCount() = list.size

    fun updateList(updatedList: List<String>) {
        list.clear()
        list.addAll(updatedList)
        notifyItemRangeRemoved(0, updatedList.size)
    }
}