package com.example.muscle_up_app.presentation.mainMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muscle_up_app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


    fun onLogoutClicked() {
        userRepository.logout()
    }
}
