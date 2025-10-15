package com.bor3y.core.file_manager

import android.content.Context

interface AudioFileManager {

    suspend fun saveFile(context: Context, audioUrl: String): String?

    suspend fun deleteFile(filePath: String): Boolean
}