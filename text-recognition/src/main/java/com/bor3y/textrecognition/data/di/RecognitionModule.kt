package com.bor3y.textrecognition.data.di

import com.bor3y.textrecognition.data.recognizer.ImageTextRecognizer
import com.bor3y.textrecognition.data.recognizer.MLKitTextRecognizer
import com.bor3y.textrecognition.data.repository.TextRecognizerRepositoryImpl
import com.bor3y.textrecognition.domain.repository.TextRecognizerRepository
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

    @Binds
    @Singleton
    abstract fun provideTextRecognizerRepository(
        textRecognizerRepositoryImpl: TextRecognizerRepositoryImpl
    ): TextRecognizerRepository
}