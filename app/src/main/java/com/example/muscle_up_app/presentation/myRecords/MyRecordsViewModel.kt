//package com.example.muscle_up_app.presentation.myRecords
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.muscle_up_app.data.repository.PersonalRecordRepository
//import com.example.muscle_up_app.data.repository.UserRepository
//import com.example.muscle_up_app.domain.model.Exercise
//import com.example.muscle_up_app.domain.model.PersonalRecord
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.ValueEventListener
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
////@HiltViewModel
////class MyRecordsViewModel @Inject constructor(
////    private val personalRecordRepository: PersonalRecordRepository,
////    private val userRepository: UserRepository
////) : ViewModel() {
////
////    private val _personalRecords = MutableStateFlow<Map<Exercise, Int>>(emptyMap())
////    val personalRecords: StateFlow<Map<Exercise, Int>> = _personalRecords
////    var benchPressRecord: PersonalRecord? by mutableStateOf(null)
////        private set
////    var shoulderPressRecord: PersonalRecord? by mutableStateOf(null)
////        private set
////    var snatchRecord: PersonalRecord? by mutableStateOf(null)
////        private set
////    var cleanRecord: PersonalRecord? by mutableStateOf(null)
////        private set
////    var deadliftRecord: PersonalRecord? by mutableStateOf(null)
////        private set
////    init {
////        fetchPersonalRecords()
////        viewModelScope.launch {
////            benchPressRecord = personalRecordRepository.getPersonalRecordByExerciseName("Bench Press")
////            shoulderPressRecord = personalRecordRepository.getPersonalRecordByExerciseName("Shoulder Press")
////            snatchRecord = personalRecordRepository.getPersonalRecordByExerciseName("Snatch")
////            cleanRecord = personalRecordRepository.getPersonalRecordByExerciseName("Clean")
////            deadliftRecord = personalRecordRepository.getPersonalRecordByExerciseName("Deadlift")
////        }
////    }
////
////    private fun fetchPersonalRecords() {
////        viewModelScope.launch {
////            val personalRecordsFromDb = personalRecordRepository.getPersonalRecordsFromUser()
////            personalRecordsFromDb.addValueEventListener(object : ValueEventListener {
////                override fun onDataChange(snapshot: DataSnapshot) {
////                    val newPersonalRecords = mutableMapOf<Exercise, Int>()
////                    snapshot.children.forEach { recordSnapshot ->
////                        val exercise = recordSnapshot.child("exercise").getValue(Exercise::class.java)
////                        val weight = recordSnapshot.child("weight").getValue(Int::class.java)
////                        if (exercise != null && weight != null) {
////                            newPersonalRecords[exercise] = weight
////                        }
////                    }
////                    _personalRecords.value = newPersonalRecords
////                }
////
////                override fun onCancelled(error: DatabaseError) {
////                    // Handle error
////                }
////            })
////        }
////    }
////
////    fun addOrUpdatePersonalRecord(exercise: Exercise, weight: Int) {
////        viewModelScope.launch {
////            val currentRecords = _personalRecords.value.toMutableMap()
////            currentRecords[exercise] = weight
////            personalRecordRepository.addOrUpdatePersonalRecord(exercise, weight)
////            _personalRecords.value = currentRecords
////        }
////    }
////}
////
//@HiltViewModel
//class MyRecordsViewModel(private val personalRecordRepository: PersonalRecordRepository) : ViewModel() {
//
//    val benchPressMax = personalRecordRepository.getPersonalRecordByExerciseName("Bench Press")?.weight
//    val shoulderPressMax = personalRecordRepository.getPersonalRecordByExerciseName("Shoulder Press")?.weight
//    val snatchMax = personalRecordRepository.getPersonalRecordByExerciseName("Snatch")?.weight
//    val cleanMax = personalRecordRepository.getPersonalRecordByExerciseName("Clean")?.weight
//    val deadliftMax = personalRecordRepository.getPersonalRecordByExerciseName("Deadlift")?.weight
//
//    fun updateMax(exerciseName: String, maxWeight: Int) {
//        val personalRecord = PersonalRecord(exercise = Exercise(name = exerciseName), weight = maxWeight)
//        CoroutineScope(viewModelScope.coroutineContext).launch {
//            personalRecordRepository.addPersonalRecord(personalRecord)
//        }
//    }
//}

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
        }
    }
}