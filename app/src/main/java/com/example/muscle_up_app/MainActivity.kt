package com.example.muscle_up_app

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.muscle_up_app.data.repository.UserRepository
import com.example.muscle_up_app.presentation.login.LoginScreen
import com.example.muscle_up_app.presentation.login.LoginViewModel
import com.example.muscle_up_app.presentation.mainMenu.MainMenu
import com.example.muscle_up_app.presentation.myRecords.MyRecords
import com.example.muscle_up_app.presentation.trainings.Trainings
import com.example.muscle_up_app.presentation.ui.theme.Muscle_up_appTheme
import com.example.muscle_up_app.presentation.weightCalculator.WeightCalculator
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MuscleUpApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()

            Muscle_up_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable("mainMenu") { MainMenu(navController) }
                        composable("trainings") { Trainings(navController) }
                        composable("weightCalculator") { WeightCalculator(navController) }
                        composable("myPRs") { MyRecords(navController) }
                    }
                }
            }
        }
    }
}
