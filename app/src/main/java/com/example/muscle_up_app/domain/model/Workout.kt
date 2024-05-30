package com.example.muscle_up_app.domain.model

data class Workout(
    val warmUpExercises: List<Exercise> = emptyList(),
    val mainExercises: List<Exercise>  = emptyList(),
    val coolDownExercises: List<Exercise>  = emptyList()
)