package com.bor3y.dictations_list.data.worker.sync_manager

import android.content.Context

interface DataSyncManager {

    suspend fun sync(context: Context): Boolean
}