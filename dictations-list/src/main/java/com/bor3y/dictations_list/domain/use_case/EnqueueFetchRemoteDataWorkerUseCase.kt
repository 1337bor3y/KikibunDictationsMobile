package com.bor3y.dictations_list.domain.use_case

import com.bor3y.dictations_list.domain.repository.DictationsListRepository
import javax.inject.Inject

class EnqueueFetchRemoteDataWorkerUseCase @Inject constructor(
    private val dictationsListRepository: DictationsListRepository
) {
    operator fun invoke() {
        dictationsListRepository.enqueueFetchRemoteDataWorker()
    }
}