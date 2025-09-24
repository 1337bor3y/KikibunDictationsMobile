package com.bor3y.dictation_detail.presentation

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bor3y.text_accuracy_lib.TextAccuracy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DictationDetailViewModel @Inject constructor() : ViewModel() {

    private var _state = MutableStateFlow(DictationDetailState())
    val state = _state.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    private val textAccuracy: TextAccuracy = TextAccuracy()

    init {
        mediaPlayer = MediaPlayer()
    }

    fun onEvent(event: DictationDetailEvent) {
        when (event) {
            is DictationDetailEvent.ChangePlaybackSpeed -> changePlaybackSpeed(event.context)

            is DictationDetailEvent.SetTranscription -> {
                _state.update {
                    it.copy(
                        transcription = event.transcription
                    )
                }
            }

            is DictationDetailEvent.SetFilePaths -> {
                _state.update {
                    it.copy(
                        audioFileDictation = event.audioFileDictation,
                        audioFileNormal = event.audioFileNormal
                    )
                }

                if ((mediaPlayer?.duration ?: 0) == 0) {
                    setupMediaPlayer(event.context, event.audioFileNormal)
                }
            }

            is DictationDetailEvent.SeekTo -> seekTo(event.position)

            DictationDetailEvent.TogglePlayPause -> togglePlayPause()

            DictationDetailEvent.FindAccuracy -> findAccuracy()

            is DictationDetailEvent.SetTypedText -> _state.update {
                it.copy(
                    typedText = event.text
                )
            }

            DictationDetailEvent.HideAccuracyDialog -> _state.update {
                it.copy(
                    showAccuracyDialog = false
                )
            }
        }
    }

    private fun changePlaybackSpeed(context: Context) {
        _state.update {
            it.copy(
                isNormalSpeed = !state.value.isNormalSpeed
            )
        }

        if (state.value.isNormalSpeed) {
            state.value.audioFileNormal?.let {
                setupMediaPlayer(context, it)
            }
        } else {
            state.value.audioFileDictation?.let {
                setupMediaPlayer(context, it)
            }
        }
    }

    private fun setupMediaPlayer(context: Context, filePath: String) {
        mediaPlayer?.apply {
            reset()
            setDataSource(context, Uri.fromFile(File(filePath)))
            prepareAsync()
            setOnPreparedListener {
                _state.update { state ->
                    state.copy(
                        duration = mediaPlayer?.duration?.toLong() ?: 0L,
                        isPlaying = false,
                        currentPosition = 0L
                    )
                }
            }
            setOnCompletionListener {
                seekTo(0L)
                _state.update { state ->
                    state.copy(
                        isPlaying = false
                    )
                }
            }
        }
    }

    private fun togglePlayPause() {
        mediaPlayer?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                _state.update { it.copy(isPlaying = false) }
            } else {
                mediaPlayer.start()
                _state.update { it.copy(isPlaying = true) }
                updatePlaybackPosition()
            }
        }
    }

    private fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position.toInt())
        _state.update { it.copy(currentPosition = position) }
    }

    private fun updatePlaybackPosition() {
        viewModelScope.launch {
            while (mediaPlayer?.isPlaying == true) {
                _state.update {
                    it.copy(currentPosition = mediaPlayer?.currentPosition?.toLong() ?: 0L)
                }
                delay(1000L)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
    }

    private fun findAccuracy() {
        _state.update {
            it.copy(
                accuracyResult = textAccuracy.findAccuracy(
                    actualText = state.value.transcription,
                    userText = state.value.typedText
                ),
                showAccuracyDialog = true
            )
        }
    }
}