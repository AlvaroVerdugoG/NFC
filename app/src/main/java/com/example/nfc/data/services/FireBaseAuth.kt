package com.example.nfc.data.services

import com.example.nfc.model.Either
import com.example.nfc.model.error.NFCError
import com.example.nfc.model.error.Success
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseAuth @Inject constructor() :
    FireBaseAuthService {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override suspend fun register(email: String, password: String): Either<NFCError, Success> {
        return try {
            val task = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (task.user != null) {
                Either.Right(Success)
            } else {
                Either.Left(NFCError.Default)
            }
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError)
        }
    }

    override suspend fun signIn(email: String, password: String): Either<NFCError, Success> {
        return try {
            val task = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (task.user != null) {
                Either.Right(Success)
            } else {
                Either.Left(NFCError.Default)
            }
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError)
        }
    }
}