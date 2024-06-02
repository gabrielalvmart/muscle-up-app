package com.example.muscle_up_app.data.repository


import android.util.Log
import com.example.muscle_up_app.domain.model.Training
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingRepository @Inject constructor(
    val databaseReference: DatabaseReference,
    val userRepository: UserRepository
) {

    private val trainingsReference: DatabaseReference = databaseReference.child("trainings")

    fun addTraining(training: Training): String {
        val userId = userRepository.getCurrentUserId()
        val userTrainingsReference = trainingsReference.child(userId)
        val trainingId = userTrainingsReference.push().key
        trainingId?.let {
            userTrainingsReference.child(it).setValue(training)
        }
        return trainingId ?: ""
    }

    // Function to get trainings associated with the current user's UID
    fun getTrainingsFromUser(): DatabaseReference {
        val userId = userRepository.getCurrentUserId()
        return trainingsReference.child(userId)
    }
}