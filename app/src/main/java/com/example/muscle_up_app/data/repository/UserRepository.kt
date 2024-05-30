package com.example.muscle_up_app.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) {

    private val usersReference: DatabaseReference = firebaseDatabase.reference.child("users")

    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Log.d("muscles", result.user!!.email.toString())
            result.user
        } catch (e: Exception) {
            Log.d("muscles", "Exception: " + e.message)
            null
        }
    }


    fun logout() {
        firebaseAuth.signOut()
    }

    fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid ?: "ERROR_WITH_ID"
    }
}
