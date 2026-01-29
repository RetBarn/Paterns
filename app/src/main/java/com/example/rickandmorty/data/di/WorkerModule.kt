package com.example.rickandmorty.data.di
//
//import android.content.Context
//import androidx.work.WorkManager
//import com.example.rickandmort.CancelAllNotificationsUseCase
//import com.example.rickandmorty.domain.CharactersRepository
//import com.example.rickandmorty.domain.ussCase.ScheduleDailyNotificationUseCase
//import com.example.rickandmorty.domain.ussCase.ScheduleNotificationUseCase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object WorkerModule {
//
//    @Provides
//    @Singleton
//    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
//        return WorkManager.getInstance(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideScheduleNotificationUseCase(
//        repository: CharactersRepository
//    ): ScheduleNotificationUseCase {
//        return ScheduleNotificationUseCase(repository)
//    }
//
//    @Provides
//    @Singleton
//    fun provideScheduleDailyNotificationUseCase(
//        repository: CharactersRepository
//    ): ScheduleDailyNotificationUseCase {
//        return ScheduleDailyNotificationUseCase(repository)
//    }
//
//    @Provides
//    @Singleton
//    fun provideCancelNotificationsUseCase(
//        repository: CharactersRepository
//    ): CancelAllNotificationsUseCase {
//        return CancelAllNotificationsUseCase(repository)
//    }
//}