package com.example.muscle_up_app.data.repository

import com.example.muscle_up_app.domain.model.PersonalRecord
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalRecordRepository @Inject constructor(
    val databaseReference: DatabaseReference,
    val userRepository: UserRepository
) {

    private val personalRecordsReference: DatabaseReference = databaseReference.child("personalRecords")

    suspend fun getPersonalRecordByExerciseName(exerciseName: String): PersonalRecord? {
        val userId = userRepository.getCurrentUserId()
        val deferred = CompletableDeferred<PersonalRecord?>()

        val query = personalRecordsReference.child(userId).orderByChild("exercise/name").equalTo(exerciseName)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (recordSnapshot in snapshot.children) {
                        val personalRecord = recordSnapshot.getValue(PersonalRecord::class.java)
                        deferred.complete(personalRecord)
                        return
                    }
                }
                deferred.complete(null)
            }

            override fun onCancelled(error: DatabaseError) {
                deferred.complete(null)
            }
        })

        return withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    fun addPersonalRecord(personalRecord: PersonalRecord): String {
        val userId = userRepository.getCurrentUserId()
        val userRecordsReference = personalRecordsReference.child(userId)
        val recordId = userRecordsReference.push().key
        recordId?.let {
            userRecordsReference.child(it).setValue(personalRecord)
        }
        return recordId ?: ""
    }
    fun updatePersonalRecord(exerciseName: String, personalRecord: PersonalRecord) {
        val userId = userRepository.getCurrentUserId()
        val userRecordsReference = personalRecordsReference.child(userId)

        // Realizar una consulta para obtener el recordId del ejercicio especificado
        val query = userRecordsReference.orderByChild("exercise/name").equalTo(exerciseName)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (recordSnapshot in snapshot.children) {
                        val recordId = recordSnapshot.key
                        recordId?.let {
                            userRecordsReference.child(it).setValue(personalRecord)
                            return
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}