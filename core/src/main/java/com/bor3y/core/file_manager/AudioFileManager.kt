package com.bor3y.core.file_manager

import android.content.Context

interface AudioFileManager {

    fun saveFile(context: Context, audioUrl: String): String?

    fun deleteFile(filePath: String): Boolean
}