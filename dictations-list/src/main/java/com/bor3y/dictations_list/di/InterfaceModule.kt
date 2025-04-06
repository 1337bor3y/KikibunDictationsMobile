package com.bor3y.dictations_list.di

import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.local.RoomLocalDataSource
import com.bor3y.dictations_list.data.remote.RemoteDataSource
import com.bor3y.dictations_list.data.remote.RetrofitRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {

    @Binds
    @Singleton
    abstract fun provideLocalDataSource(
        roomLocalDataSource: RoomLocalDataSource
    ): LocalDataSource

    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(
        retrofitRemoteDataSource: RetrofitRemoteDataSource
    ): RemoteDataSource
}