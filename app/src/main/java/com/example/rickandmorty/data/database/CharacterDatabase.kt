package com.example.rickandmorty.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmorty.data.database.converters.EpisodesConverter
import com.example.rickandmorty.data.database.converters.GenderConverter
import com.example.rickandmorty.data.database.converters.StatusConverter
import com.example.rickandmorty.data.database.dao.CharactersDao
import com.example.rickandmorty.data.database.dao.FavoriteCharacterDao
import com.example.rickandmorty.data.database.entities.CharacterEntity
import com.example.rickandmorty.data.database.entities.FavoriteCharacterEntity


@Database(
    entities = [
        FavoriteCharacterEntity::class,
        CharacterEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    EpisodesConverter::class,
    GenderConverter::class,
    StatusConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteCharacterDao
    abstract fun cacheDao(): CharactersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rick_and_morty_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}