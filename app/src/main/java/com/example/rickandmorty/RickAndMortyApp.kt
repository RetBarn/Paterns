package com.example.rickandmorty

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickAndMortyApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}