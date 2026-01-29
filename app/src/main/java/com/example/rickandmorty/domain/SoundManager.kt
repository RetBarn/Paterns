package com.example.rickandmorty.domain

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import com.example.rickandmorty.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private val _isPlaying = MutableStateFlow(false)
    private val _volumeLevel = MutableStateFlow(1.0f)
    private val _isLooping = MutableStateFlow(false)

    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    val volumeLevel: StateFlow<Float> = _volumeLevel.asStateFlow()
    val isLooping: StateFlow<Boolean> = _isLooping.asStateFlow()

    private fun getSoundResource(): Int {
        return try {
            val resourceId = R.raw.character_sound
            val res = context.resources
            val typedValue = android.util.TypedValue()
            res.getValue(resourceId, typedValue, true)
            resourceId
        } catch (e: Exception) {
            -1
        }
    }

    fun playCharacterSound() {
        stopPlaying()

        try {
            val soundResId = getSoundResource()

            mediaPlayer = if (soundResId != -1) {
                MediaPlayer.create(context, soundResId)
            } else {
                MediaPlayer().apply {
                    setDataSource(context, Settings.System.DEFAULT_NOTIFICATION_URI)
                    prepare()
                }
            }

            mediaPlayer?.apply {
                setVolume(_volumeLevel.value, _volumeLevel.value)

                isLooping = _isLooping.value

                setOnCompletionListener {
                    if (_isLooping.value && isLooping) {
                        seekTo(0)
                        start()
                    } else {
                        _isPlaying.value = false
                        releasePlayer()
                    }
                }

                setOnErrorListener { mp, what, extra ->
                    _isPlaying.value = false
                    releasePlayer()
                    false
                }

                start()
                _isPlaying.value = true
                setSystemVolumeToMax()
            }
        } catch (e: Exception) {
            _isPlaying.value = false
            releasePlayer()
            e.printStackTrace()
        }
    }

    private fun setSystemVolumeToMax() {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
        } catch (e: Exception) {
        }
    }

    fun setVolume(level: Float) {
        _volumeLevel.value = level.coerceIn(0f, 1.0f)
        mediaPlayer?.setVolume(level, level)
    }

    fun increaseVolume() {
        val newVolume = (_volumeLevel.value + 0.1f).coerceAtMost(1.0f)
        setVolume(newVolume)
    }

    fun decreaseVolume() {
        val newVolume = (_volumeLevel.value - 0.1f).coerceAtLeast(0f)
        setVolume(newVolume)
    }

    fun setLooping(looping: Boolean) {
        _isLooping.value = looping
        mediaPlayer?.isLooping = looping
    }

    fun toggleLooping() {
        setLooping(!_isLooping.value)
    }

    fun stopPlaying() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            releasePlayer()
        }
        _isPlaying.value = false
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            stopPlaying()
        } else {
            playCharacterSound()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        _isPlaying.value = false
    }

    fun resume() {
        mediaPlayer?.start()
        _isPlaying.value = true
    }

    private fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun isSoundAvailable(): Boolean {
        return getSoundResource() != -1
    }
}