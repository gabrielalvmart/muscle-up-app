package com.example.muscle_up_app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(firebaseAuth: FirebaseAuth, firebaseDatabase: FirebaseDatabase): UserRepository {
        return UserRepository(firebaseAuth, firebaseDatabase)
    }

}