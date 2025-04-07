package com.bor3y.dictations_list.data.worker

import android.content.Context

interface DataSyncManager {

    suspend fun sync(context: Context): Boolean
}