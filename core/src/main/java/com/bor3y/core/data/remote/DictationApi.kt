package com.bor3y.core.data.remote

import com.bor3y.core.data.remote.dto.DictationDetailDto
import com.bor3y.core.until.ApiConfig
import com.bor3y.core.data.remote.dto.DictationItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictationApi {

    @GET(ApiConfig.DICTATIONS_LIST_ENDPOINT)
    suspend fun getDictations(): Result<List<DictationItemDto>>

    @GET(ApiConfig.DICTATIONS_LIST_ENDPOINT + "/{id}")
    suspend fun getDictationDetail(@Path("id") id: String): Result<DictationDetailDto>
}