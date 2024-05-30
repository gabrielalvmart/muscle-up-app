package com.example.muscle_up_app.presentation.mainMenu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun MainMenu(navController: NavController) {
    val viewModel: MainMenuViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly, // Distribute space evenly between rows
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Main Menu",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Distribute space evenly between columns
        ) {
            RoundedSquareWithIconAndDescription(
                modifier = Modifier.size(150.dp), // Set a fixed size for the rounded square
                icon = { Image(painter = painterResource(com.example.muscle_up_app.R.drawable.dumbbell), contentDescription = "Dumbbell Icon") },
                description = "Trainings",
                onClick = { navController.navigate("trainings")  }
            )
            RoundedSquareWithIconAndDescription(
                modifier = Modifier.size(150.dp), // Set a fixed size for the rounded square
                icon = {
                    Image(
                        painter = painterResource(com.example.muscle_up_app.R.drawable.calcualtor),
                        contentDescription = "Weight Calculator",
                        modifier = Modifier.height(80.dp))},
                description = "Weight Calc",
                onClick = { navController.navigate("weightCalculator") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Distribute space evenly between columns
        ) {
            RoundedSquareWithIconAndDescription(
                modifier = Modifier.size(150.dp), // Set a fixed size for the rounded square
                icon = {
                    Image(
                        painter = painterResource(com.example.muscle_up_app.R.drawable.leaderboard),
                        contentDescription = "My PRs",
                        modifier = Modifier.height(64.dp))},
                description = "My PRs",
                onClick = { navController.navigate("myPRs") }
            )
            RoundedSquareWithIconAndDescription(
                modifier = Modifier.size(150.dp), // Set a fixed size for the rounded square
                icon = {
                    Image(
                        painter = painterResource(com.example.muscle_up_app.R.drawable.logout),
                        contentDescription = "Dumbbell Icon",
                        modifier = Modifier.height(80.dp))},
                description = "Logout",
                onClick = {
                    viewModel.onLogoutClicked()
                    navController.navigate("login")
                }
            )
        }
    }
}

@Composable
fun RoundedSquareWithIconAndDescription(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    description: String,
    onClick: () -> Unit
) {
    val borderColor = Color.Black // Change the color of the border as needed
    val borderWidth = 2.dp // Change the width of the border as needed

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .border(
                BorderStroke(width = borderWidth, brush = SolidColor(borderColor)),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                icon()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = description,
                    color = Color.Black
                )
            }
        }
    }
}