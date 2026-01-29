package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.CharactersRepository
import javax.inject.Inject


class ScheduleDailyNotificationUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    operator fun invoke() {
        repository.scheduleDailyNotification()
    }
}