package com.example.nfc.data.preference

import com.example.nfc.model.User

interface Preference {
    fun getBooleanUserData(): Boolean
    fun setBooleanUserData(isChecked: Boolean)
    fun getEmail(): String
    fun setEmail(email: String)
}