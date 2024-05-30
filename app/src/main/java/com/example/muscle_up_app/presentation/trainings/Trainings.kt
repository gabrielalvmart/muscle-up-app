package com.example.muscle_up_app.presentation.trainings

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.muscle_up_app.domain.model.Exercise
import com.example.muscle_up_app.domain.model.Training
import com.example.muscle_up_app.domain.model.Workout


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Trainings(
    navController: NavController
) {
    val viewModel: TrainingsViewModel =  hiltViewModel()
    var showDialog by remember { mutableStateOf(false) }
    val trainings by viewModel.trainings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.navigate("mainMenu") },
            ) {
                Text("<")
            }
            Text(
                text = "Trainings",
                style = MaterialTheme.typography.headlineMedium,
            )
            Button(
                onClick = { showDialog = true }
            ) {
                Text(text = "+ Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



        Spacer(modifier = Modifier.height(16.dp))

        TrainingsTable(trainings = trainings)

        if (showDialog) {
            AddTrainingDialog(
                onDismissRequest = { showDialog = false },
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun TrainingsTable(trainings: List<Training>) {
    var showExercisesDialog by remember { mutableStateOf(false) }
    var selectedWorkout by remember { mutableStateOf<Workout?>(null) }

    Column {
        trainings.forEach { training ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(text = training.title, style = MaterialTheme.typography.bodyLarge)
                    Text(text = training.date, style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = {
                    // Get exercises for this training
                    selectedWorkout = training.workout
                    showExercisesDialog = true
                }) {
                    Text(text = "Show Exercises")
                }
            }
        }
    }

    if (showExercisesDialog && selectedWorkout != null) {
        ShowExercisesDialog(
            workout = selectedWorkout!!,
            onDismissRequest = { showExercisesDialog = false }
        )
    }
}


@Composable
fun ShowExercisesDialog(workout: Workout, onDismissRequest: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }

    val tabTitles = listOf("Warm-up", "Routine", "Cooldown")

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Exercises", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(8.dp))

                // Tabs
                TabRow(selectedTabIndex = selectedTab) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab content
                when (selectedTab) {
                    0 -> ExerciseList(exercises = workout.warmUpExercises)
                    1 -> ExerciseList(exercises = workout.mainExercises)
                    2 -> ExerciseList(exercises = workout.coolDownExercises)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun ExerciseList(exercises: List<Exercise>) {
    Column {
        exercises.forEach { exercise ->
            Text(text = exercise.name, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}