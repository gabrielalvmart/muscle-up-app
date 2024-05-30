package com.example.muscle_up_app.domain.model

data class Exercise(val name: String = "", val functionalGroup: String = "")

val exerciseTypeList = listOf("Push","Pull", "Legs", "Core")

val warmupExerciseList = listOf(
    Exercise("Jumping Jacks", "Warm-up"),
    Exercise("High Knees", "Warm-up"),
    Exercise("Butt Kicks", "Warm-up"),
    Exercise("Arm Circles (Forward & Backward)", "Warm-up"),
    Exercise("Torso Twists", "Warm-up"),
    Exercise("Walking Lunges", "Warm-up"),
    Exercise("Arm Swings (Forward & Backward)", "Warm-up"),
    Exercise("Ankle Circles", "Warm-up"),
    Exercise("Neck Rolls", "Warm-up"),
    Exercise("Jumping Jacks (light)", "Warm-up")
)

val coolDownExerciseList = listOf(
    Exercise("Hamstring Stretch", "Cool-down"),
    Exercise("Quad Stretch", "Cool-down"),
    Exercise("Calf Stretch", "Cool-down"),
    Exercise("Chest Stretch", "Cool-down"),
    Exercise("Tricep Stretch", "Cool-down"),
    Exercise("Shoulder Stretch", "Cool-down"),
    Exercise("Lower Back Stretch", "Cool-down"),
    Exercise("Neck Stretch", "Cool-down"),
    Exercise("Piriformis Stretch", "Cool-down"),
    Exercise("Pigeon Pose", "Cool-down")
)

val exercisesList = listOf(

    // Push exercises
    Exercise("Push-ups", "Push"),
    Exercise("Diamond push-ups", "Push"),
    Exercise("Decline push-ups", "Push"),
    Exercise("Incline push-ups", "Push"),
    Exercise("Pike push-ups", "Push"),
    Exercise("Push-up rows (each arm)", "Push"),
    Exercise("Bench press (barbell)", "Push"),
    Exercise("Dumbbell press", "Push"),
    Exercise("Overhead press (barbell)", "Push"),
    Exercise("Arnold press (dumbbells)", "Push"),
    Exercise("Tricep dips", "Push"),
    Exercise("Tricep extensions (dumbbell)", "Push"),

    // Pull exercises
    Exercise("Pull-ups", "Pull"),
    Exercise("Chin-ups", "Pull"),
    Exercise("Inverted rows", "Pull"),
    Exercise("Lat pulldown (machine)", "Pull"),
    Exercise("Seated cable rows", "Pull"),
    Exercise("Bent-over barbell rows", "Pull"),
    Exercise("Dumbbell rows", "Pull"),
    Exercise("Bicep curls (barbell)", "Pull"),
    Exercise("Hammer curls (dumbbell)", "Pull"),
    Exercise("Concentration curls (dumbbell)", "Pull"),

    // Legs
    Exercise("Squats (bodyweight)", "Legs"),
    Exercise("Squats (barbell)", "Legs"),
    Exercise("Goblet squats (dumbbell)", "Legs"),
    Exercise("Lunges (bodyweight)", "Legs"),
    Exercise("Walking lunges (dumbbell)", "Legs"),
    Exercise("Bulgarian split squats (dumbbell)", "Legs"),
    Exercise("Deadlifts (barbell)", "Legs"),
    Exercise("Romanian deadlifts (dumbbell)", "Legs"),
    Exercise("Calf raises (bodyweight)", "Legs"),
    Exercise("Calf raises (standing, machine)", "Legs"),
    Exercise("Hamstring curls (machine)", "Legs"),

    // Core
    Exercise("Plank", "Core"),
    Exercise("Side plank (each side)", "Core"),
    Exercise("Hollow body hold", "Core"),
    Exercise("Russian twists", "Core"),
    Exercise("Crunches", "Core"),
    Exercise("Bicycle crunches", "Core"),
    Exercise("Mountain climbers", "Core"),
    Exercise("Anti-rotational press (medicine ball)", "Core"),
    Exercise("Dead bug", "Core"),
    Exercise("Leg raises", "Core"),


)

// Function to get exercises by type
fun getExercisesByType(type: String): List<Exercise> {
    return exercisesList.filter { it.functionalGroup.equals(type, ignoreCase = true) }
}

fun getExerciseTypes(): List<String> {
    return exerciseTypeList
}

fun getRandomExercises(type: String, count: Int): List<Exercise> {
    val exercisesByType = getExercisesByType(type)
    return exercisesByType.shuffled().take(count)
}

fun getRandomWarmupExercises(count: Int): List<Exercise> {
    return warmupExerciseList.shuffled().take(count)
}

fun getRandomCooldownExercises(count: Int): List<Exercise> {
    return coolDownExerciseList.shuffled().take(count)
}

fun generateWorkoutPlan(primaryType: String, countWarmUp: Int = 3, countMain: Int = 10, countCoolDown: Int = 3): Workout {
    val warmUpExercises = getRandomWarmupExercises(countWarmUp)
    val mainExercises = getRandomExercises(primaryType, countMain)
    val coolDownExercises = getRandomCooldownExercises(countCoolDown)

    return Workout(
        warmUpExercises = warmUpExercises,
        mainExercises = mainExercises,
        coolDownExercises = coolDownExercises
    )
}
