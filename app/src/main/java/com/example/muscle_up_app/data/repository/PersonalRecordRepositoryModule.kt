package com.example.muscle_up_app.data.repository

import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersonalRecordRepositoryModule {

    @Provides
    @Singleton
    fun providePersonalRecordRepository(databaseReference: DatabaseReference, userRepository: UserRepository): PersonalRecordRepository {
        return PersonalRecordRepository(databaseReference, userRepository)
    }
}