package com.example.muscle_up_app.presentation.myRecords

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun MyRecords(
    navController: NavController
) {
    val viewModel: MyRecordsViewModel = hiltViewModel()
    val personalRecords by viewModel.personalRecords.collectAsState()
    val context = LocalContext.current

    val tempWeights = remember { mutableStateMapOf<String, Int>() }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Personal Records", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        for (exercise in listOf("Bench Press(barbell)", "Shoulder Press", "Snatch", "Clean", "Deadlift")) {
            val record = personalRecords[exercise]
            val weight = record?.weight
            if (weight != null) {
                ExerciseRecordRow(
                    exerciseName = exercise,
                    initialWeight = weight,
                    onWeightChange = { newWeight ->
                        tempWeights[exercise] = newWeight
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                tempWeights.forEach { (exercise, weight) ->
                    viewModel.updatePersonalRecord(exercise, weight)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(244, 67, 54, 255),
                disabledContainerColor = Color(255, 152, 0, 255)
            )
        ) {
            Text(text = "Guardar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(244, 67, 54, 255),
                disabledContainerColor = Color(255, 152, 0, 255)
            )
        ) {
            Text(text = "Regresar")
        }
    }

    val savedChanges by viewModel.savedChanges
    if (savedChanges) {
        Toast.makeText(context, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
        viewModel.savedChanges.value = false
    }
}

@Composable
fun ExerciseRecordRow(
    exerciseName: String,
    initialWeight: Int,
    onWeightChange: (Int) -> Unit
) {
    var weight by remember { mutableStateOf(initialWeight) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = exerciseName, style = MaterialTheme.typography.bodyLarge)
        BasicTextField(
            value = weight.toString(),
            onValueChange = { value ->
                weight = value.toIntOrNull() ?: 0
                weight = weight.coerceAtLeast(0) // Asegurarse de que el peso no sea negativo
                onWeightChange(weight)
            },
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .width(60.dp)
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        )
    }
}