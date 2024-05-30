package com.example.muscle_up_app.domain.model

data class Training(
    val id: String = "",
    val title: String = "",
    val date: String = "",
    val workout: Workout = Workout(),
    val userID: String = ""
)