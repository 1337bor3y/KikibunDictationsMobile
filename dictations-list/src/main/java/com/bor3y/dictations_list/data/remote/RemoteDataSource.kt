package com.bor3y.dictations_list.data.remote

import com.bor3y.dictations_list.data.remote.model.DictationDetailRemote
import com.bor3y.dictations_list.data.remote.model.DictationItemRemote

interface RemoteDataSource {

    suspend fun getDictations(): Result<List<DictationItemRemote>>

    suspend fun getDictationDetail(id: String): Result<DictationDetailRemote>
}