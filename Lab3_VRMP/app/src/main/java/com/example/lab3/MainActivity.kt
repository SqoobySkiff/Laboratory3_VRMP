package com.example.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var btnCart: Button
    private lateinit var btnRecommend: Button
    private lateinit var adapter: ProductAdapter
    private var isShowingRecommendations = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCart = findViewById(R.id.btnCart)
        btnRecommend = findViewById(R.id.btnRecommend)
        val btnLogout: Button = findViewById(R.id.btnLogout)
        val btnOrders: Button = findViewById(R.id.btnOrders)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCatalog)
        val btnFavorites: Button = findViewById(R.id.btnFavorites)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(
            products = CatalogManager.products,
            onAddToCartClick = { product ->
                CartManager.addToCart(product)
                updateCartButton()
                Toast.makeText(this, "${product.name} додано в кошик!", Toast.LENGTH_SHORT).show()
            },
            onFavoriteToggle = { product, isChecked ->
                if (isChecked) {
                    UserManager.currentUser?.favoriteIds?.add(product.id)
                } else {
                    UserManager.currentUser?.favoriteIds?.remove(product.id)
                }
            }
        )
        recyclerView.adapter = adapter

        btnFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        btnRecommend.setOnClickListener {
            if (isShowingRecommendations) {
                adapter.updateList(CatalogManager.products)
                btnRecommend.text = "Рекомендації"
                isShowingRecommendations = false
            } else {
                val recommended = getRecommendations()
                if (recommended.size != CatalogManager.products.size) {
                    adapter.updateList(recommended)
                    btnRecommend.text = "Всі товари"
                    isShowingRecommendations = true
                }
            }
        }

        btnCart.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
        btnOrders.setOnClickListener { startActivity(Intent(this, OrdersActivity::class.java)) }
        btnLogout.setOnClickListener {
            UserManager.logout()
            CartManager.clearCart()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        updateCartButton()
        adapter.notifyDataSetChanged()
    }

    private fun updateCartButton() {
        val totalItems = CartManager.getTotalItems()
        btnCart.text = "Кошик ($totalItems)"
    }

    private fun getRecommendations(): List<Product> {
        val currentUser = UserManager.currentUser ?: return CatalogManager.products
        val myOrders = currentUser.orders

        if (myOrders.isEmpty()) {
            Toast.makeText(this, "Зробіть покупку, щоб ми могли підібрати рекомендації!", Toast.LENGTH_SHORT).show()
            return CatalogManager.products
        }

        val myPurchasedIds = myOrders.flatMap { order -> order.items.map { it.product.id } }.toSet()
        val recommendationScores = mutableMapOf<Product, Int>()

        for (otherUser in UserManager.usersList) {
            if (otherUser.email == currentUser.email) continue

            val otherUserPurchasedProducts = otherUser.orders
                .flatMap { order -> order.items.map { it.product } }
                .toSet()

            val otherUserPurchasedIds = otherUserPurchasedProducts.map { it.id }.toSet()
            val commonItems = myPurchasedIds.intersect(otherUserPurchasedIds)

            if (commonItems.isNotEmpty()) {
                for (product in otherUserPurchasedProducts) {
                    if (product.id !in myPurchasedIds) {
                        recommendationScores[product] = recommendationScores.getOrDefault(product, 0) + 1
                    }
                }
            }
        }

        if (recommendationScores.isEmpty()) {
            Toast.makeText(this, "Немає даних від інших користувачів. Показуємо базові рекомендації.", Toast.LENGTH_SHORT).show()
            return CatalogManager.products.filter { it.id !in myPurchasedIds }
        }

        Toast.makeText(this, "Рекомендації на основі вибору інших покупців!", Toast.LENGTH_SHORT).show()
        return recommendationScores.entries
            .sortedByDescending { it.value }
            .map { it.key }
    }
}