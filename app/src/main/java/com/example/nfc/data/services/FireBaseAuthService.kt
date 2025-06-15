package com.example.nfc.data.services

import com.example.nfc.model.Either
import com.example.nfc.model.error.NFCError
import com.example.nfc.model.error.Success

interface FireBaseAuthService {
    suspend fun register(email: String, password: String): Either<NFCError, Success>
    suspend fun signIn(email: String, password: String): Either<NFCError, Success>
    fun signOut(): Either<NFCError, Success>
    suspend fun changePassword(password: String): Either<NFCError, Success>
    suspend fun reSendVerificationEmail(): Either<NFCError, Success>
    suspend fun forgetPassword(email: String): Either<NFCError, Success>
    suspend fun deleteAccount(): Either<NFCError, Success>
}