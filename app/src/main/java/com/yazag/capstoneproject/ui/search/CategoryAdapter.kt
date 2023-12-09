package com.yazag.capstoneproject.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yazag.capstoneproject.data.model.response.Category
import com.yazag.capstoneproject.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val onCategoryClick: (Int) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryViewHolder.CategoryDiffUtilCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onCategoryClick
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(getItem(position))

    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val onCategoryClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            with(binding){
                tvTitleCategory.text = category.title
            }
        }
        class CategoryDiffUtilCallBack : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }
}