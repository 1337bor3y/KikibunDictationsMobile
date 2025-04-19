package com.bor3y.core.logger

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SimpleLogger @Inject constructor() : Logger {

    override fun logError(tag: String, msg: String) {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedTime = currentTime.format(formatter)

        // Later log with Firebase Crashlytics
        Log.d(tag, "$formattedTime: $msg")
    }
}