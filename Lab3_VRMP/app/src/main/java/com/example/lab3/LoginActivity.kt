package com.example.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()

            if (UserManager.login(email, pass)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Невірний логін або пароль!", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()

            if (email.isNotBlank() && pass.isNotBlank()) {
                if (UserManager.register(email, pass)) {
                    Toast.makeText(this, "Реєстрація успішна! Тепер увійдіть.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Користувач з таким логіном вже існує!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}