package com.example.muscle_up_app.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.muscle_up_app.domain.model.Training
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainingRepository @Inject constructor(
    val databaseReference: DatabaseReference,
    val userRepository: UserRepository
) {

    private val trainingsReference: DatabaseReference = databaseReference.child("trainings")
    private val prsReference: DatabaseReference = databaseReference.child("prs")

    fun addTraining(training: Training): String {
        val userId = userRepository.getCurrentUserId()
        val userTrainingsReference = trainingsReference.child(userId)
        val trainingId = userTrainingsReference.push().key
        trainingId?.let {
            userTrainingsReference.child(it).setValue(training)
        }
        return trainingId ?: ""
    }

    fun getTrainingsFromUser(): DatabaseReference {
        val userId = userRepository.getCurrentUserId()
        return trainingsReference.child(userId)
    }

    fun getRecordsLiveData(): LiveData<Map<String, Int>> {
        val userId = userRepository.getCurrentUserId()
        val prsLiveData = MutableLiveData<Map<String, Int>>()
        prsReference.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val prsMap = snapshot.value as? Map<String, Int> ?: emptyMap()
                prsLiveData.value = prsMap
            }

            override fun onCancelled(error: DatabaseError) {
                prsLiveData.value = emptyMap() // Se establece un valor predeterminado
                Log.e(TAG, "Error al obtener los registros de entrenamiento: ${error.message}")
            }
        })
        return prsLiveData
    }

    // Agrega este método para actualizar el registro de un ejercicio específico
    fun updateRecord(exercise: String, pr: Int) {
        val userId = userRepository.getCurrentUserId()
        val prsMap = mutableMapOf<String, Any?>()
        prsMap[exercise] = pr
        prsReference.child(userId).updateChildren(prsMap)
    }

    companion object {
        private const val TAG = "TrainingRepository"
    }
}
