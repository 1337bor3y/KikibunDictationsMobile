package com.bor3y.core.di

import com.bor3y.core.file_manager.AudioFileManager
import com.bor3y.core.file_manager.IternalStorageAudioFileManager
import com.bor3y.core.logger.Logger
import com.bor3y.core.logger.SimpleLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun provideLogger(
        simpleLogger: SimpleLogger
    ): Logger

    @Binds
    @Singleton
    abstract fun provideAudioFileManager(
        iternalStorageAudioFileManager: IternalStorageAudioFileManager
    ): AudioFileManager
}