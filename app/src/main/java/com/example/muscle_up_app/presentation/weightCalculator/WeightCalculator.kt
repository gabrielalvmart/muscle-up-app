package com.example.muscle_up_app.presentation.weightCalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun WeightCalculator(navController: NavController){
    var weightInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val weights = listOf(45.0, 35.0, 25.0, 15.0, 10.0, 5.0, 2.5)
    val barWeight = 45.0

    fun distributeWeight(totalWeight: Double): String {
        val remainingWeight = totalWeight - barWeight
        if (remainingWeight < 0) return "El peso total debe ser mayor que el peso de la barra (45 lbs)."

        var currentWeight = remainingWeight
        val distribution = mutableMapOf<Double, Int>()

        for (weight in weights) {
            val count = ((currentWeight / weight).toInt() / 2) * 2
            if (count > 0) {
                distribution[weight] = count
                currentWeight -= count * weight
            }
        }

        return buildString {
            append("Barra: $barWeight lbs\n")
            distribution.forEach { (weight, count) ->
                append("Discos de $weight lbs: $count\n")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it },
                label = { Text("Peso total (lbs)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color(244, 67, 54, 255),
                        disabledContainerColor = Color(255, 152, 0, 255)
                    )
                ) {
                    Text(text = "Regresar")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        val weight = weightInput.toDoubleOrNull()
                        if (weight != null) {
                            result = distributeWeight(weight)
                        } else {
                            result = "Por favor, ingrese un peso válido."
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color(244, 67, 54, 255),
                        disabledContainerColor = Color(255, 152, 0, 255)
                    )
                ) {
                    Text(text = "Calcular")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = result,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}