package com.example.nfc.data.services

import android.content.Context
import com.example.nfc.R
import com.example.nfc.model.Either
import com.example.nfc.model.User
import com.example.nfc.model.error.NFCError
import com.example.nfc.model.error.Success
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseStorage @Inject constructor(@ApplicationContext private val context: Context) :
    FireBaseStorageService {
    private val firestore = FirebaseFirestore.getInstance()
    override suspend fun saveUserData(user: User): Either<NFCError, Success> {
        return try {
            val docRef = firestore.document("users/${user.email}")
            val snapshot = docRef.get().await()
            if (!snapshot.exists()) {
                docRef.set(user.toCloudMap()).await()
            }
            Either.Right(Success)

        } catch (e: Exception) {
            val error = handleError(e)
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override suspend fun updateProfilePhoto(email: String,
                                            newPhotoUrl: String): Either<NFCError, Success> {
        return try {
            firestore.document("users/${email}").update("profilePhotoUrl", newPhotoUrl).await()
            delay(1000)
            Either.Right(Success)
        } catch (e: Exception) {
            val error = handleError(e)
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override suspend fun fetchUserData(email: String): Either<NFCError, User> {
        return try {
            val snapshot = firestore.document("users/${email}").get().await()
            val user = snapshot.toObject(User::class.java)
            Either.Right(user!!)
        } catch (e: Exception) {
            val error = handleError(e)
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override suspend fun checkEmailRegistered(email: String): Either<NFCError, Boolean> {
        return try {
            val snapshot = firestore.collection("users").whereEqualTo("email", email).get().await()
            Either.Right(snapshot.documents.isNotEmpty())
        } catch (e: Exception) {
            val error = handleError(e)
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override suspend fun deleteDataFromEmail(email: String): Either<NFCError, Success> {
        return try {
            firestore.document("users/${email}").delete().await()
            Either.Right(Success)
        } catch (e: Exception) {
            val error = handleError(e)
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    override suspend fun saveDeleteReason(reasons: List<String>): Either<NFCError, Success> {
        return try {
            val data = hashMapOf("reasons" to reasons)
            firestore.collection("delete_reasons").add(data).await()
            Either.Right(Success)
        } catch (e: Exception) {
            val error = handleError(e)
            Either.Left(NFCError.FireBaseError(error))
        }
    }

    private fun handleError(exception: Exception): String {
        return if (exception is FirebaseFirestoreException) {
            when (exception.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> context.getString(R.string.permission_denied)
                FirebaseFirestoreException.Code.NOT_FOUND -> context.getString(R.string.not_found)
                FirebaseFirestoreException.Code.UNAVAILABLE -> context.getString(R.string.unavailable)
                FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> context.getString(R.string.deadline_exceeded)
                FirebaseFirestoreException.Code.ABORTED -> context.getString(R.string.aborted)
                else -> context.getString(R.string.unexpected_error)
            }
        } else {
            context.getString(R.string.unexpected_error)
        }
    }
}