package com.example.muscle_up_app.presentation.trainings

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.muscle_up_app.domain.model.Training
import com.example.muscle_up_app.domain.model.generateWorkoutPlan
import com.example.muscle_up_app.domain.model.getExerciseTypes
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.muscle_up_app.domain.model.getRandomExercises
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTrainingDialog(onDismissRequest: () -> Unit, viewModel: TrainingsViewModel) {
    var title by remember { mutableStateOf("") }
    var selectedExerciseType by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var isFormSubmitted by remember { mutableStateOf(false) }

    val isFormValid by remember {
        derivedStateOf {
            title.isNotEmpty() && selectedDate.isNotEmpty() && selectedExerciseType.isNotEmpty()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Add Training", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = title,
                    onValueChange = { newValue -> title = newValue },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isFormSubmitted && title.isEmpty()
                )
                if (isFormSubmitted && title.isEmpty()) {
                    Text(
                        text = "Title is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Date", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                DatePicker(
                    initialValue = selectedDate,
                    onValueChange = { newDate ->
                        selectedDate = newDate
                    }
                )
                if (isFormSubmitted && selectedDate.isEmpty()) {
                    Text(
                        text = "Date is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Functional group", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))

                DropdownField(getExerciseTypes()) { selected ->
                    selectedExerciseType = selected
                }
                if (isFormSubmitted && selectedExerciseType.isEmpty()) {
                    Text(
                        text = "Functional group is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Add Button
                Button(
                    onClick = {
                        isFormSubmitted = true
                        if (isFormValid) {
                            val training = Training(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                date = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                                workout = generateWorkoutPlan(primaryType = selectedExerciseType, 5, 10, 5),
                                userID = viewModel.getCurrentUserID()
                            )
                            viewModel.addTraining(training)
                            onDismissRequest()
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = isFormValid
                ) {
                    Text("Add")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    initialValue: String,
    onValueChange: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    pattern: String = "yyyy-MM-dd",
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val context = LocalContext.current
    var date by remember { mutableStateOf(if (initialValue.isNotBlank()) LocalDate.parse(initialValue, formatter) else LocalDate.now()) }

    val dialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = LocalDate.of(year, month + 1, dayOfMonth)
            onValueChange(date.format(formatter))
        },
        date.year,
        date.monthValue - 1,
        date.dayOfMonth,
    )

    TextField(
        value = date.format(formatter),
        onValueChange = { newDate ->
            date = LocalDate.parse(newDate, formatter)
            onValueChange(newDate)
        },
        enabled = false,
        modifier = Modifier.clickable { dialog.show() },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = false // Do not show error here
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Column {
        TextField(
            value = selectedOption,
            onValueChange = { },
            readOnly = true,
            label = { Text("Select Option") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
                }
            },
            isError = false // Do not show error here
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}
