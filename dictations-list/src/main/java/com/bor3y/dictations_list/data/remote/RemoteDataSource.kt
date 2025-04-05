package com.bor3y.dictations_list.data.remote

import com.bor3y.dictations_list.data.remote.model.DictationItemRemote

interface RemoteDataSource {

    suspend fun getDictations(): Result<List<DictationItemRemote>>
}