package com.example.rickandmorty.data.di

import android.content.Context
import androidx.work.WorkManager
import com.example.rickandmort.CancelAllNotificationsUseCase
import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.ussCase.ScheduleDailyNotificationUseCase
import com.example.rickandmorty.domain.ussCase.ScheduleNotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.database.dao.CharactersDao
import com.example.rickandmorty.data.database.dao.FavoriteCharacterDao
import com.example.rickandmorty.data.manager.SharedFavoritesManager
import com.example.rickandmorty.data.repository.CharactersRepositoryImpl
import com.example.rickandmorty.domain.ussCase.*
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCharactersRepository(
        api: RickAndMortyApi,
        cacheDao: CharactersDao,
        favoriteDao: FavoriteCharacterDao,
        favoritesManager: SharedFavoritesManager,
        @ApplicationContext context: Context
    ): CharactersRepository {
        return CharactersRepositoryImpl(api, cacheDao, favoriteDao, favoritesManager, context)
    }

    @Provides
    @Singleton
    fun provideGetCharactersUseCase(
        repository: CharactersRepository
    ): GetCharactersUseCase {
        return GetCharactersUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetCharacterDetailsUseCase(
        repository: CharactersRepository
    ): GetCharacterDetailsUseCase {
        return GetCharacterDetailsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideSearchCharactersUseCase(
        repository: CharactersRepository
    ): SearchCharactersUseCase {
        return SearchCharactersUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetCharactersWithFiltersUseCase(
        repository: CharactersRepository
    ): GetCharactersWithFiltersUseCase {
        return GetCharactersWithFiltersUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetCachedCharactersUseCase(
        repository: CharactersRepository
    ): GetCachedCharactersUseCase {
        return GetCachedCharactersUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideSaveCharactersUseCase(
        repository: CharactersRepository
    ): SaveCharactersUseCase {
        return SaveCharactersUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(
        repository: CharactersRepository
    ): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideIsFavoriteUseCase(
        repository: CharactersRepository
    ): IsFavoriteUseCase {
        return IsFavoriteUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoritesUseCase(
        repository: CharactersRepository
    ): GetFavoritesUseCase {
        return GetFavoritesUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoritesStreamUseCase(
        repository: CharactersRepository
    ): GetFavoritesStreamUseCase {
        return GetFavoritesStreamUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateCharacterUseCase(
        repository: CharactersRepository
    ): UpdateCharacterUseCase {
        return UpdateCharacterUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideClearCacheUseCase(
        repository: CharactersRepository
    ): ClearCacheUseCase {
        return ClearCacheUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetCharacterWithCacheUseCase(
        getCachedCharacters: GetCachedCharactersUseCase,
        getCharacters: GetCharactersUseCase,
        saveCharacters: SaveCharactersUseCase,
        clearCache: ClearCacheUseCase,
        getFavorites: GetFavoritesUseCase
    ): GetCharacterWithCacheUseCase {
        return GetCharacterWithCacheUseCase(
            getCachedCharacters,
            getCharacters,
            saveCharacters,
            clearCache,
            getFavorites
        )
    }
}