package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.UUID

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val tvCartContents: TextView = findViewById(R.id.tvCartContents)
        val tvTotalPrice: TextView = findViewById(R.id.tvTotalPrice)
        val btnCheckout: Button = findViewById(R.id.btnCheckout)

        if (CartManager.cartItems.isEmpty()) {
            tvCartContents.text = "Ваш кошик порожній"
            btnCheckout.isEnabled = false
        } else {
            val stringBuilder = StringBuilder()
            for (item in CartManager.cartItems) {
                stringBuilder.append("${item.product.name} (x${item.quantity}) - ${item.product.price * item.quantity} $\n\n")
            }
            tvCartContents.text = stringBuilder.toString()
            btnCheckout.isEnabled = true
        }

        tvTotalPrice.text = "Разом: ${CartManager.getTotalPrice()} $"

        btnCheckout.setOnClickListener {
            val order = Order(
                orderId = UUID.randomUUID().toString().take(6),
                items = CartManager.cartItems.toList(),
                totalPrice = CartManager.getTotalPrice()
            )

            UserManager.currentUser?.orders?.add(order)

            Handler(Looper.getMainLooper()).postDelayed({
                order.status = OrderStatus.COMPLETED
            }, 10000)

            CartManager.clearCart()
            Toast.makeText(this, "Замовлення #${order.orderId} оформлено!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}