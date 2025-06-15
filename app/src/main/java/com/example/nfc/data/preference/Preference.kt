package com.example.nfc.data.preference


interface Preference {
    fun getBooleanUserData(): Boolean
    fun setBooleanUserData(isChecked: Boolean)
    fun getEmail(): String
    fun setEmail(email: String)
}