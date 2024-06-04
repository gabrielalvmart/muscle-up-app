
package com.example.muscle_up_app.presentation.myRecords

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muscle_up_app.data.repository.PersonalRecordRepository
import com.example.muscle_up_app.domain.model.Exercise
import com.example.muscle_up_app.domain.model.PersonalRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecordsViewModel @Inject constructor(
    private val personalRecordRepository: PersonalRecordRepository
) : ViewModel() {

    private val _personalRecords = MutableStateFlow<Map<String, PersonalRecord>>(emptyMap())
    val personalRecords: StateFlow<Map<String, PersonalRecord>> = _personalRecords
    val savedChanges = mutableStateOf(false)

    init {
        fetchPersonalRecords()
    }

    private fun fetchPersonalRecords() {
        viewModelScope.launch {
            val exercises = listOf("Bench Press(barbell)", "Shoulder Press", "Snatch", "Clean", "Deadlift")
            val records = mutableMapOf<String, PersonalRecord>()
            for (exercise in exercises) {
                val record = personalRecordRepository.getPersonalRecordByExerciseName(exercise)
                record?.let {
                    records[exercise] = it
                }
            }
            _personalRecords.value = records
        }
    }

    fun updatePersonalRecord(exerciseName: String, weight: Int) {
        viewModelScope.launch {
            val existingRecord = _personalRecords.value[exerciseName]
            val updatedRecord = existingRecord?.copy(weight = weight) ?: PersonalRecord(exercise = Exercise(name = exerciseName), weight = weight)
            _personalRecords.value = _personalRecords.value.toMutableMap().apply {
                this[exerciseName] = updatedRecord
            }
            if (existingRecord == null) {
                personalRecordRepository.addPersonalRecord(updatedRecord)
            } else {
                personalRecordRepository.updatePersonalRecord(exerciseName, updatedRecord)
            }
            savedChanges.value = true
        }
    }
}