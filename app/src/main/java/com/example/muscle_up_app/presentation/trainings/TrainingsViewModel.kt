package com.example.muscle_up_app.presentation.trainings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muscle_up_app.data.repository.TrainingRepository
import com.example.muscle_up_app.data.repository.UserRepository
import com.example.muscle_up_app.domain.model.Exercise
import com.example.muscle_up_app.domain.model.Training
import com.example.muscle_up_app.domain.model.Workout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _trainings = MutableStateFlow<List<Training>>(emptyList())
    val trainings: StateFlow<List<Training>> = _trainings

    init {
        fetchTrainings()
    }

    private fun fetchTrainings() {
        viewModelScope.launch {
            val trainingsFromDb = trainingRepository.getTrainingsFromUser()
            trainingsFromDb.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newTrainings = snapshot.children.mapNotNull { it.getValue(Training::class.java) }
                    _trainings.value = newTrainings
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    fun addTraining(training: Training) {
        viewModelScope.launch {
            val trainingId = trainingRepository.addTraining(training)
            if (trainingId.isNotEmpty()) {
                // If training was added successfully to the database, update the UI
                val updatedTrainings = _trainings.value.toMutableList().apply {
                    add(training.copy(id = trainingId))
                }
                _trainings.value = updatedTrainings
            } else {
                Log.e("Wooiii", "Errorinski")
            }
        }
    }

    fun getWorkoutForTraining(training: Training): Workout {
        return training.workout
    }

    fun getCurrentUserID(): String {
        return userRepository.getCurrentUserId()
    }
}