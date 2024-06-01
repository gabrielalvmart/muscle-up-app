package com.example.muscle_up_app.presentation.myRecords

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muscle_up_app.data.repository.TrainingRepository
import com.example.muscle_up_app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecordsViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
) : ViewModel() {
    fun getTrainingRecords(): LiveData<Map<String, Int>> {
        return trainingRepository.getRecordsLiveData()
    }

    fun updateRecord(exercise: String, pr: Int) {
        trainingRepository.updateRecord(exercise, pr)
    }
}