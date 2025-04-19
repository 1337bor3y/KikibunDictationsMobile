package com.bor3y.dictations_list.di

import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.local.RoomLocalDataSource
import com.bor3y.dictations_list.data.remote.RemoteDataSource
import com.bor3y.dictations_list.data.remote.RetrofitRemoteDataSource
import com.bor3y.dictations_list.data.repository.DictationsListRepositoryImpl
import com.bor3y.dictations_list.data.worker.sync_manager.DataSyncManager
import com.bor3y.dictations_list.data.worker.sync_manager.RemoteLocalDataSyncManager
import com.bor3y.dictations_list.domain.repository.DictationsListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DictationsListModule {

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

    @Binds
    @Singleton
    abstract fun provideDataSyncManager(
        remoteLocalDataSyncManager: RemoteLocalDataSyncManager
    ): DataSyncManager

    @Binds
    @Singleton
    abstract fun provideDictationsListRepository(
        dictationsListRepositoryImpl: DictationsListRepositoryImpl
    ): DictationsListRepository
}