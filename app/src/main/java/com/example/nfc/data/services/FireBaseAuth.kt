package com.example.nfc.data.services

import android.content.Context
import android.util.Log
import com.example.nfc.R
import com.example.nfc.model.Either
import com.example.nfc.model.error.NFCError
import com.example.nfc.model.error.Success
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseAuth @Inject constructor(@ApplicationContext private val context: Context) :
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
            val error = handleError(e.message ?: context.getString(R.string.unexpected_error))
            Either.Left(NFCError.FireBaseError(error))
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
                        NFCError.FireBaseError(context.getString(R.string.error_email_not_verified))
                    )
                }
            } else {
                Either.Left(NFCError.Default)
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
            val error = handleError(e.message ?: context.getString(R.string.unexpected_error))
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override fun signOut(): Either<NFCError, Success> {
        return try {
            firebaseAuth.signOut()
            Either.Right(Success)
        } catch (e: Exception) {
            val error = handleError(e.message ?: context.getString(R.string.unexpected_error))
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override suspend fun changePassword(password: String): Either<NFCError, Success> {
        val user = firebaseAuth.currentUser
        return try {
            user?.updatePassword(password)
            Either.Right(Success)
        } catch (e: Exception) {
            val error = handleError(e.message ?: context.getString(R.string.unexpected_error))
            Either.Left(NFCError.FireBaseError(error))
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
            val error = handleError(e.message ?: context.getString(R.string.unexpected_error))
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override suspend fun forgetPassword(email: String): Either<NFCError, Success> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Either.Right(Success)
        } catch (e: Exception) {
            val error = handleError(e.message ?: context.getString(R.string.unexpected_error))
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override suspend fun deleteAccount(): Either<NFCError, Success> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                user.delete().await()
                Either.Right(Success)
            } else {
                Either.Left(NFCError.FireBaseError(context.getString(R.string.unexpected_error)))
            }

        } catch (e: Exception) {
            val error = handleError(e.message ?: context.getString(R.string.unexpected_error))
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    fun getEmail(): String {
        firebaseAuth.currentUser?.reload()
        return firebaseAuth.currentUser?.email.orEmpty()
    }

    private fun handleError(errorMessage: String): String {
        return when {
            errorMessage.contains("String is empty", ignoreCase = true) ->
                context.getString(R.string.error_string_empty)
            errorMessage.contains("wrong password", ignoreCase = true) ->
                context.getString(R.string.error_password_wrong)
            errorMessage.contains("Invalid credential", ignoreCase = true) ->
                context.getString(R.string.error_invalid_credential)
            errorMessage.contains("auth/user-disabled", ignoreCase = true) ->
                context.getString(R.string.error_user_disabled)
            errorMessage.contains("usernot found", ignoreCase = true) ->
                context.getString(R.string.error_user_not_found)
            errorMessage.contains("too many requests", ignoreCase = true) ->
                context.getString(R.string.error_too_many_tries)
            errorMessage.contains("network error", ignoreCase = true) ->
                context.getString(R.string.error_conection)
            else ->
                context.getString(R.string.unexpected_error)
        }
    }

}