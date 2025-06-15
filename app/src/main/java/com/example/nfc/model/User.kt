package com.example.nfc.model


data class User(var name: String = "",
                var lastName: String = "",
                var email: String = "",
                var password: String = "",
                var confirmPassword: String = "",
                var profilePhotoUrl: String = "") {
    fun toCloudMap(): Map<String, Any> {
        return mapOf("name" to name,
            "lastName" to lastName,
            "email" to email,
            "profilePhotoUrl" to profilePhotoUrl.toString())
    }

    fun isCorrect(): Boolean {
        if (email.isNotEmpty() && name.isNotEmpty() && lastName.isNotEmpty()) {
            return true
        } else {
            return false
        }
    }
}