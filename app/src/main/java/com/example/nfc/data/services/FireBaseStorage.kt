package com.example.nfc.data.services


import android.net.Uri
import androidx.compose.animation.core.snap
import com.example.nfc.model.Either
import com.example.nfc.model.User
import com.example.nfc.model.error.NFCError
import com.example.nfc.model.error.Success
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FireBaseStorage @Inject constructor() : FireBaseStorageService {
    private val firestore = FirebaseFirestore.getInstance()
    override suspend fun saveUserData(user: User): Either<NFCError, Success> {
        return try {
            val docRef = firestore.collection("users").document(user.email)
            val snapshot = docRef.get().await()
            if (!snapshot.exists()) {
                docRef.set(user.toCloudMap()).await()
            }
            Either.Right(Success)

        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun updateProfilePhoto(
        email: String,
        newPhotoUrl: String
    ): Either<NFCError, Success> {
        return try {
            firestore.collection("users")
                .document(email)
                .update("profilePhotoUrl", newPhotoUrl)
                .await()
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun fetchUserData(email: String): Either<NFCError, User> {
        return try {
            val snapshot = firestore.collection("users").document(email).get().await()
            val user = snapshot.toObject(User::class.java)
            Either.Right(user!!)
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun checkEmailRegistered(email: String): Either<NFCError, Boolean> {
        return try {
            val snapshot = firestore.collection("users").whereEqualTo("email", email).get().await()
            Either.Right(snapshot.documents.isNotEmpty())
        }catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun deleteDataFromEmail(email: String): Either<NFCError, Success> {
        return try{
            firestore.collection("users").document(email).delete().await()
            Either.Right(Success)
        }catch (e: Exception){
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }
    override suspend fun saveDeleteReason(reasons: List<String>): Either<NFCError, Success> {
        return try {
            val data = hashMapOf(
                "reasons" to reasons
            )
            firestore.collection("delete_reasons").add(data).await()
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(NFCError.FireBaseError(e.message ?: "Unknown error"))
        }
    }
}