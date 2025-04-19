package com.bor3y.core.di

import android.content.Context
import androidx.room.Room
import com.bor3y.core.until.DatabaseConfig
import com.bor3y.core.data.local.DictationDatabase
import com.bor3y.core.data.local.DictationsListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDictationsListDatabase(@ApplicationContext context: Context): DictationDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = DictationDatabase::class.java,
            name = DatabaseConfig.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDictationsListDao(db: DictationDatabase): DictationsListDao {
        return db.dictationListDao
    }
}