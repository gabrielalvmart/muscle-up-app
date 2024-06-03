package com.example.muscle_up_app.domain.model

class PersonalRecord(
    var exercise: Exercise = Exercise(),
    var weight: Int = 0
){
    fun copy(exercise: Exercise = this.exercise, weight: Int = this.weight): PersonalRecord {
        return PersonalRecord(exercise, weight)
    }

}