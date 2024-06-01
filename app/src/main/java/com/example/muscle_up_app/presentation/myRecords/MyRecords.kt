package com.example.muscle_up_app.presentation.myRecords

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController




@Composable
fun MyRecords(navController: NavController) {
    val viewModel: MyRecordsViewModel = hiltViewModel()
    var showDialog by remember { mutableStateOf(false) }
    val records by viewModel.getTrainingRecords().observeAsState(emptyMap())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TrainingItem("Bench Press", records["Bench Press"] ?: 0) { pr ->
            viewModel.updateRecord("Bench Press", pr)
        }
        TrainingItem("Shoulder Press", records["Shoulder Press"] ?: 0) { pr ->
            viewModel.updateRecord("Shoulder Press", pr)
        }
        TrainingItem("Snatch", records["Snatch"] ?: 0) { pr ->
            viewModel.updateRecord("Snatch", pr)
        }
        TrainingItem("Clean", records["Clean"] ?: 0) { pr ->
            viewModel.updateRecord("Clean", pr)
        }
        TrainingItem("Deadlift", records["Deadlift"] ?: 0) { pr ->
            viewModel.updateRecord("Deadlift", pr)
        }
    }
}

@Composable
fun TrainingItem(exercise: String, pr: Int, onUpdate: (Int) -> Unit) {
    var newPr by remember { mutableStateOf(pr) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = exercise)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = newPr.toString(),
            onValueChange = { newPr = it.toIntOrNull() ?: 0 },
            label = { Text(text = "PR (lbs)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onUpdate(newPr) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Save")
        }
    }
}