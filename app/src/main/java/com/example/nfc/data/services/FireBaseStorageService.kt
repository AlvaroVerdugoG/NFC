package com.example.nfc.data.services

import com.example.nfc.model.Either
import com.example.nfc.model.User
import com.example.nfc.model.error.NFCError
import com.example.nfc.model.error.Success

interface FireBaseStorageService {
    suspend fun saveUserData(user: User): Either<NFCError, Success>
    suspend fun updateProfilePhoto(email: String, newPhotoUrl: String): Either<NFCError, Success>
    suspend fun fetchUserData(email: String): Either<NFCError, User>
    suspend fun checkEmailRegistered(email: String): Either<NFCError, Boolean>
    suspend fun deleteDataFromEmail(email: String): Either<NFCError, Success>
    suspend fun saveDeleteReason(reasons: List<String>): Either<NFCError, Success>
}