package com.example.nfc.data.services

import com.example.nfc.model.Either
import com.example.nfc.model.error.NFCError
import com.example.nfc.model.error.Success

interface FireBaseAuthService {
    suspend fun register(email:String, password: String) : Either<NFCError, Success>
    suspend fun signIn(email: String, password: String) : Either<NFCError, Success>
}