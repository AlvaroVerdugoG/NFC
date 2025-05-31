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
            val user = task.user
            if (user != null) {
                user.sendEmailVerification().await()
                Either.Right(Success)
            } else {
                Either.Left(NFCError.Default)
            }
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun signIn(email: String, password: String): Either<NFCError, Success> {
        return try {
            val task = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = task.user
            if (user != null) {
                user.reload().await()
                if (user.isEmailVerified) {
                    Either.Right(Success)
                } else {
                    firebaseAuth.signOut()
                    Either.Left(
                        NFCError.FireBaseError("Email not verified.\nPlease check your email and follow the confirmation link.")
                    )
                }
            } else {
                Either.Left(NFCError.Default)
            }
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }

    override fun signOut(): Either<NFCError, Success> {
        return try {
            firebaseAuth.signOut()
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error."))
        }
    }

    override suspend fun changePassword(password: String): Either<NFCError, Success> {
        val user = firebaseAuth.currentUser
        return try {
            user?.updatePassword(password)
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error."))
        }

    }

    override suspend fun reSendVerificationEmail(): Either<NFCError, Success> {
        val user = firebaseAuth.currentUser
        return try {
            if (user != null && !user.isEmailVerified) {
                user.sendEmailVerification().await()
                Either.Right(Success)
            } else {
                Either.Left(NFCError.Default)
            }
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error."))
        }
    }

    override suspend fun forgetPassword(email: String): Either<NFCError, Success> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun deleteAccount(): Either<NFCError, Success> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.delete().await()
                Either.Right(Success)
            } else{
                Either.Left(NFCError.FireBaseError("Unknown error"))
            }

        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }

    fun getEmail(): String {
        firebaseAuth.currentUser?.reload()
        return firebaseAuth.currentUser?.email.orEmpty()
    }
}