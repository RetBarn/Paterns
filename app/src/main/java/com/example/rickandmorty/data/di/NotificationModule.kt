package com.example.rickandmorty.data.di


import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.example.rickandmort.CancelAllNotificationsUseCase
import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.database.dao.CharactersDao
import com.example.rickandmorty.data.database.dao.FavoriteCharacterDao
import com.example.rickandmorty.data.manager.SharedFavoritesManager
import com.example.rickandmorty.data.repository.CharactersRepositoryImpl
import com.example.rickandmorty.domain.CharactersRepository
import com.example.rickandmorty.domain.ussCase.ScheduleDailyNotificationUseCase
import com.example.rickandmorty.domain.ussCase.ScheduleNotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCharactersRepositoryImpl(
        api: RickAndMortyApi,
        cacheDao: CharactersDao,
        favoriteDao: FavoriteCharacterDao,
        favoritesManager: SharedFavoritesManager,
        @ApplicationContext context: Context
    ): CharactersRepositoryImpl {
        return CharactersRepositoryImpl(api, cacheDao, favoriteDao, favoritesManager, context)
    }

    @Provides
    @Singleton
    fun provideScheduleNotificationUseCase(
        repository: CharactersRepositoryImpl
    ): ScheduleNotificationUseCase {
        return ScheduleNotificationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideScheduleDailyNotificationUseCase(
        repository: CharactersRepositoryImpl
    ): ScheduleDailyNotificationUseCase {
        return ScheduleDailyNotificationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCancelNotificationsUseCase(
        repository: CharactersRepositoryImpl
    ): CancelAllNotificationsUseCase {
        return CancelAllNotificationsUseCase(repository)
    }
}