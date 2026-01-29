package com.example.rickandmorty.data.manager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedFavoritesManager @Inject constructor() {

    private val _favoriteUpdates = MutableSharedFlow<FavoriteUpdate>()
    val favoriteUpdates: SharedFlow<FavoriteUpdate> = _favoriteUpdates

    suspend fun notifyFavoriteChanged(characterId: Long, isFavorite: Boolean) {
        _favoriteUpdates.emit(FavoriteUpdate(characterId, isFavorite))
    }

    data class FavoriteUpdate(val characterId: Long, val isFavorite: Boolean)
}