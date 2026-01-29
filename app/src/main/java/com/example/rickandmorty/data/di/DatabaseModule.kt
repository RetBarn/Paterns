package com.example.rickandmorty.data.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.data.database.AppDatabase
import com.example.rickandmorty.data.database.dao.CharactersDao
import com.example.rickandmorty.data.database.dao.FavoriteCharacterDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCharactersDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "characters.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCharactersDao(database: AppDatabase): CharactersDao {
        return database.cacheDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteCharacterDao(database: AppDatabase): FavoriteCharacterDao {
        return database.favoriteDao()
    }
}