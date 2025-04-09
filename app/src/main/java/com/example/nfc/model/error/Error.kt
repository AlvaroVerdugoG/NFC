package com.example.nfc.model.error

sealed class NFCError {
    data object Default: NFCError()
    data object FireBaseError: NFCError()
}
object Success