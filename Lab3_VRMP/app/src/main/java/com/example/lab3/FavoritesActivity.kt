package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val recyclerView: RecyclerView = findViewById(R.id.rvFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userFavorites = CatalogManager.products.filter {
            UserManager.currentUser?.favoriteIds?.contains(it.id) == true
        }

        adapter = ProductAdapter(
            products = userFavorites,
            onAddToCartClick = { product ->
                CartManager.addToCart(product)
                Toast.makeText(this, "${product.name} додано в кошик!", Toast.LENGTH_SHORT).show()
            },
            onFavoriteToggle = { product, isChecked ->
                if (isChecked) {
                    UserManager.currentUser?.favoriteIds?.add(product.id)
                } else {
                    UserManager.currentUser?.favoriteIds?.remove(product.id)
                }
                updateUI()
            }
        )
        recyclerView.adapter = adapter
    }

    private fun updateUI() {
        val newList = CatalogManager.products.filter {
            UserManager.currentUser?.favoriteIds?.contains(it.id) == true
        }
        adapter.updateList(newList)
    }
}