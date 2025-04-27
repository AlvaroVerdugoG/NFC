package com.example.nfc.model.error

sealed class NFCError {
    data object Default: NFCError()
    data class FireBaseError(val message: String): NFCError()
}
object Success