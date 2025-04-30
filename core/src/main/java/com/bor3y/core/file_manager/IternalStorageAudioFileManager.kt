package com.bor3y.core.file_manager

import android.content.Context
import com.bor3y.core.logger.Logger
import com.bor3y.core.until.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import javax.inject.Inject

class IternalStorageAudioFileManager @Inject constructor(
    private val logger: Logger
) : AudioFileManager {

    override suspend fun saveFile(context: Context, audioUrl: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                if (!audioUrl.endsWith(".mp3")) {
                    logger.logError(
                        Constants.LogErrors.SAVE_AUDIO_FILE_ERROR.name,
                        "Invalid file format (not .mp3)"
                    )
                    return@withContext null
                }

                val fileName = audioUrl.substringAfterLast("/")
                    .substringBefore(".mp3")
                    .replace(Regex("[^a-zA-Z0-9_]"), "_")

                val audioFile = File(context.filesDir, fileName)

                val inputStream: InputStream = URL(audioUrl).openStream()
                val outputStream: OutputStream = FileOutputStream(audioFile)

                val buffer = ByteArray(8192)
                var length: Int
                while (inputStream.read(buffer).also { length = it } != -1) {
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                inputStream.close()
                outputStream.close()

                audioFile.absolutePath
            } catch (e: Exception) {
                logger.logError(
                    Constants.LogErrors.SAVE_AUDIO_FILE_ERROR.name,
                    e.localizedMessage ?: "Unknown error"
                )
                null
            }
        }
    }

    override suspend fun deleteFile(filePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            val file = File(filePath)
           if (file.exists()) {
                try {
                    file.delete()
                } catch (e: Exception) {
                    logger.logError(
                        Constants.LogErrors.DELETE_AUDIO_FILE_ERROR.name,
                        "Exception deleting $filePath: ${e.localizedMessage}"
                    )
                    false
                }
            } else {
                true
            }
        }
    }
}