package com.bor3y.core.di

import com.bor3y.core.logger.Logger
import com.bor3y.core.logger.SimpleLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerModule {

    @Binds
    @Singleton
    abstract fun provideLogger(
        simpleLogger: SimpleLogger
    ): Logger
}