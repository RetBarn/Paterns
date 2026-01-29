package com.example.rickandmort

import com.example.rickandmorty.domain.CharactersRepository
import javax.inject.Inject

class CancelAllNotificationsUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    operator fun invoke() {
        repository.cancelAllNotifications()
    }
}