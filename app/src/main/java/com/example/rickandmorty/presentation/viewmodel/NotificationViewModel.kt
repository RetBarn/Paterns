package com.example.rickandmorty.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmort.CancelAllNotificationsUseCase
import com.example.rickandmorty.domain.ussCase.ScheduleDailyNotificationUseCase
import com.example.rickandmorty.domain.ussCase.ScheduleNotificationUseCase
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase,
    private val scheduleDailyUseCase: ScheduleDailyNotificationUseCase,
    private val cancelNotificationsUseCase: CancelAllNotificationsUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val isScheduled: Boolean = false,
        val message: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun scheduleNotification(delayMinutes: Long = 0) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                scheduleNotificationUseCase(delayMinutes)

                val msg = if (delayMinutes == 0L)
                    "✅ Тест запущен! Уведомление через 30 секунд"
                else
                    "✅ Уведомление через $delayMinutes минут"

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isScheduled = true,
                    message = msg
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "❌ Ошибка: ${e.message}"
                )
            }
        }
    }

    fun scheduleDaily() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                scheduleDailyUseCase()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "✅ Ежедневное уведомление на 12:00"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "❌ Ошибка"
                )
            }
        }
    }

    fun cancelAll() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                cancelNotificationsUseCase()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isScheduled = false,
                    message = "✅ Отменено"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "❌ Ошибка"
                )
            }
        }
    }

    fun resetMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}