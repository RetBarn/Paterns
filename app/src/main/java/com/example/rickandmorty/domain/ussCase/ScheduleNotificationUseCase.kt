package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    operator fun invoke(delayMinutes: Long = 0) {
        repository.scheduleNotification(delayMinutes)
    }
}