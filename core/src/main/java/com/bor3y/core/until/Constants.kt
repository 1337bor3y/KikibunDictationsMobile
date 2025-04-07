package com.bor3y.core.until

object Constants {
    enum class LogErrors {
        DICTATIONS_LIST_SERVER_ERROR,
        DICTATIONS_DETAIL_SERVER_ERROR
    }
    const val MAX_DICTATIONS_IN_DB = 24
    const val OLDEST_DICTATIONS_DELETE_COUNT = 6
}