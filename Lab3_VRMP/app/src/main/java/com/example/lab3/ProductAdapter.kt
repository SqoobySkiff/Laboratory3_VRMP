package com.example.lab3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCartClick: (Product) -> Unit,
    private val onFavoriteToggle: (Product, Boolean) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val cbFavorite: CheckBox = view.findViewById(R.id.cbFavorite)
        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)
    }

    fun updateList(newList: List<Product>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = products.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int) = products[oldPos].id == newList[newPos].id
            override fun areContentsTheSame(oldPos: Int, newPos: Int) = products[oldPos] == newList[newPos]
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        products = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "${product.price} $"

        holder.cbFavorite.setOnCheckedChangeListener(null)

        val isFav = UserManager.currentUser?.favoriteIds?.contains(product.id) ?: false
        holder.cbFavorite.isChecked = isFav

        holder.btnAddToCart.setOnClickListener { onAddToCartClick(product) }
        holder.cbFavorite.setOnCheckedChangeListener { _, isChecked ->
            onFavoriteToggle(product, isChecked)
        }
    }

    override fun getItemCount() = products.size
}