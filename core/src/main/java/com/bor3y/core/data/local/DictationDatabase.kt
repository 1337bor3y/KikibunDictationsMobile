package com.bor3y.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bor3y.core.data.local.entity.DictationEntity

@Database(
    entities = [DictationEntity::class],
    version = 1
)
abstract class DictationDatabase: RoomDatabase() {

    abstract val dictationListDao: DictationsListDao
}