package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class OrdersActivity : AppCompatActivity() {

    private lateinit var tvOrdersList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        tvOrdersList = findViewById(R.id.tvOrdersList)
    }

    override fun onResume() {
        super.onResume()
        val userOrders = UserManager.currentUser?.orders ?: emptyList()

        if (userOrders.isEmpty()) {
            tvOrdersList.text = "У вас ще немає замовлень."
        } else {
            val sb = java.lang.StringBuilder()
            for (order in userOrders.reversed()) {
                sb.append("Замовлення #${order.orderId}\n")
                sb.append("Статус: ${order.status.title}\n")
                sb.append("Сума: ${order.totalPrice} $\n")
                sb.append("---------------------------\n")
            }
            tvOrdersList.text = sb.toString()
        }
    }
}