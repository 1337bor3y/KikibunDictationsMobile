package com.bor3y.textrecognition.di

import com.bor3y.textrecognition.data.ImageTextRecognizer
import com.bor3y.textrecognition.data.MLKitTextRecognizer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecognitionModule {
    @Binds
    @Singleton
    abstract fun provideTextRecognizer(
        mlKitTextRecognizer: MLKitTextRecognizer
    ): ImageTextRecognizer
}