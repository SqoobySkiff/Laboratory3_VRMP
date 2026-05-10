package com.example.lab3

object UserManager {
    val usersList = mutableListOf<User>()
    var currentUser: User? = null

    fun register(email: String, pass: String): Boolean {
        if (usersList.any { it.email == email }) return false
        usersList.add(User(email, pass))
        return true
    }

    fun login(email: String, pass: String): Boolean {
        val user = usersList.find { it.email == email && it.password == pass }
        if (user != null) {
            currentUser = user
            return true
        }
        return false
    }

    fun logout() {
        currentUser = null
    }
}