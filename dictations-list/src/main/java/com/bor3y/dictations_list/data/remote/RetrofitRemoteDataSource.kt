package com.bor3y.dictations_list.data.remote

import com.bor3y.core.data.remote.DictationApi
import com.bor3y.dictations_list.data.mapper.toRemote
import com.bor3y.dictations_list.data.remote.model.DictationItemRemote
import javax.inject.Inject

class RetrofitRemoteDataSource @Inject constructor(
    private val api: DictationApi
) : RemoteDataSource {

    override suspend fun getDictations(): Result<List<DictationItemRemote>> {
        return api.getDictations().map { list -> list.map { it.toRemote() } }
    }
}