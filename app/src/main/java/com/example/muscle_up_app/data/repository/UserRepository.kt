package com.example.muscle_up_app.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepository @Inject constructor() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {

            val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.d("muscless", result.user!!.email.toString())
            result.user
        } catch (e: Exception) {
            Log.d("muscless", "Exception: " + e.message)
            null
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun logout() {
        auth.signOut()
    }
}

