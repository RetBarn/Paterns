package com.example.rickandmorty.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.rickandmorty.MainActivity
import com.example.rickandmorty.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.random.Random

@HiltWorker
class CharacterNotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val character = getRandomCharacter()
            createNotificationChannel()
            showNotification(character)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun getRandomCharacter(): Character {
        val characters = listOf(
            Character("Рик Санчез", "Гениальный ученый"),
            Character("Морти Смит", "Неуверенный подросток"),
            Character("Бет Смит", "Хирург-ветеринар"),
            Character("Джерри Смит", "Безработный"),
            Character("Саммер Смит", "Подросток"),
            Character("Мистер Жопосранчик", "Существо из другого измерения"),
            Character("Птичья личность", "Инопланетянин с планеты Птичьих людей"),
            Character("Зигги Смит", "Клон Бет"),
            Character("Кротофер", "Друг Рика из микровселенной"),
            Character("Альберт Эйнштейн", "Ученый из другой реальности")
        )

        return characters[Random.nextInt(characters.size)]
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Уведомления о персонажах",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о новых персонажах Рик и Морти"
            }

            val notificationManager = appContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(character: Character) {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(appContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Новый персонаж!")
            .setContentText("${character.name} - ${character.type}")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = appContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    data class Character(
        val name: String,
        val type: String
    )

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "rick_morty_channel"
        const val NOTIFICATION_ID = 1001
    }
}