package com.example.rickandmorty.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import com.example.rickandmorty.presentation.viewmodel.NotificationViewModel
import kotlinx.coroutines.delay

@Composable
fun NotificationScreen(
    onBackClick:() -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    // Авто-очистка сообщений
    LaunchedEffect(uiState.message) {
        if (uiState.message != null && !uiState.isLoading) {
            delay(4000)
            viewModel.resetMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Заголовок
        Text(
            text = "🔔 Тест WorkManager",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Статус
        if (uiState.isScheduled) {
            Text(
                text = "✅ Уведомление запланировано",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Кнопка 1: Быстрый тест (30 сек)
        Button(
            onClick = { viewModel.scheduleNotification() },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Загрузка...")
            } else {
                Text("⚡ Тест (30 секунд)")
            }
        }

        // Кнопка 2: 15 минут
        Button(
            onClick = { viewModel.scheduleNotification(15) },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("15 минут")
        }

        // Кнопка 3: Ежедневно
        OutlinedButton(
            onClick = { viewModel.scheduleDaily() },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Ежедневно в 12:00")
        }

        // Кнопка 4: Отмена
        if (uiState.isScheduled) {
            Button(
                onClick = { viewModel.cancelAll() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("❌ Отменить")
            }
        }

        // Сообщение
        uiState.message?.let { message ->
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = message,
                fontSize = 16.sp,
                color = if (message.startsWith("✅"))
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
        }

        // Инструкция
        Spacer(modifier = Modifier.height(48.dp))
        Card (
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("💡 Как проверить:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Нажмите 'Тест'")
                Text("2. Закройте приложение")
                Text("3. Ждите уведомление")
            }
        }
    }
}