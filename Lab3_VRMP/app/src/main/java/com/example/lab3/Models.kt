package com.example.lab3

enum class OrderStatus(val title: String) {
    PROCESSING("В обробці"),
    COMPLETED("Оброблено")
}

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    var isFavorite: Boolean = false
)

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val totalPrice: Double,
    var status: OrderStatus = OrderStatus.PROCESSING
)

data class User(
    val email: String,
    val password: String,
    val orders: MutableList<Order> = mutableListOf(),
    val favoriteIds: MutableSet<String> = mutableSetOf()
)