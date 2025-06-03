package com.example.nfc.data.preference

import android.content.SharedPreferences
import javax.inject.Inject

class CommonPreference @Inject constructor(private val sharedPreferences: SharedPreferences) :
    Preference {
    companion object {
        private const val USER_PREFS = "UserPrefs"
        private const val EMAIL_PREFS = "EmailPrefs"
    }

    override fun getBooleanUserData(): Boolean {
        return sharedPreferences.getBoolean(USER_PREFS, false)
    }

    override fun setBooleanUserData(isChecked: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(USER_PREFS, isChecked)
        editor.apply()
    }

    override fun getEmail(): String {
        return (sharedPreferences.getString(EMAIL_PREFS, "") ?: "")

    }

    override fun setEmail(email: String) {
        sharedPreferences.edit().putString(EMAIL_PREFS, email).apply()
    }

}

