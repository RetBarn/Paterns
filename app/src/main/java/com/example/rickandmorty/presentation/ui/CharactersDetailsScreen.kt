package com.example.rickandmorty.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rickandmorty.domain.SoundManager
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.CharactersStatus
import com.example.rickandmorty.presentation.viewmodel.CharacterDetailsViewModel
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material3.LinearProgressIndicator@Composable
fun CharacterDetailsScreen(
    characterId: Long,
    onBackClick: () -> Unit,
    viewModel: CharacterDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val soundManager = remember {
        SoundManager(context.applicationContext)
    }

    val isPlaying = soundManager.isPlaying.collectAsState().value
    val isLooping = soundManager.isLooping.collectAsState().value
    val volumeLevel = soundManager.volumeLevel.collectAsState().value

    LaunchedEffect(characterId) {
        viewModel.setCharacterId(characterId)
    }

    val uiState = viewModel.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }

            Text(
                text = "Персонаж",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { soundManager.toggleLooping() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isLooping) Icons.Default.Repeat
                    else Icons.Default.RepeatOne,
                    contentDescription = if (isLooping) "Выключить повтор"
                    else "Включить повтор",
                    tint = if (isLooping) Color.Blue else Color.Gray
                )
            }

            IconButton(
                onClick = { soundManager.togglePlayPause() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause
                    else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Пауза"
                    else "Воспроизвести звук",
                    tint = if (isPlaying) Color.Red else Color.Black
                )
            }

            IconButton(
                onClick = { soundManager.increaseVolume() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.VolumeUp,
                    contentDescription = "Увеличить громкость",
                    tint = if (volumeLevel > 0.5f) Color.Green else Color.Gray
                )
            }

            if (uiState.character != null) {
                IconButton(
                    onClick = viewModel::toggleFavorite,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.character!!.isFavorite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.Favorite
                        },
                        contentDescription = "Избранное",
                        tint = if (uiState.character!!.isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }

        if (isPlaying) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.VolumeDown,
                    contentDescription = "Громкость",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )

                LinearProgressIndicator(
                    progress = volumeLevel,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    color = Color.Blue,
                    trackColor = Color.LightGray
                )

                Icon(
                    Icons.Default.VolumeUp,
                    contentDescription = "Громкость",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )

                Text(
                    text = "${(volumeLevel * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color.Black
                    )
                }

                uiState.error != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.error!!,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = viewModel::retry,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text("Повторить", color = Color.White)
                        }
                    }
                }

                uiState.character != null -> {
                    CharacterDetails(
                        character = uiState.character!!,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


@Composable
fun CharacterDetails(
    character: AllCharacterDomainModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = character.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = character.status.name,
                fontSize = 16.sp,
                color = when (character.status) {
                    CharactersStatus.ALIVE -> Color.Green
                    CharactersStatus.DEAD -> Color.Red
                    CharactersStatus.UNKNOWN -> Color.Gray
                }
            )

            Text(
                text = "•",
                color = Color.Gray
            )

            Text(
                text = character.species,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoItem(title = "Тип", value = character.type.ifEmpty { "Неизвестно" })
            InfoItem(title = "Гендер", value = character.gender.name)

            if (character.location.name.isNotEmpty()) {
                InfoItem(title = "Локация", value = character.location.name)
            }

            if (character.origin.name.isNotEmpty()) {
                InfoItem(title = "Происхождение", value = character.origin.name)
            }

            if (character.episodes.isNotEmpty()) {
                InfoItem(
                    title = "Эпизоды",
                    value = character.episodes.joinToString(", ")
                )
            }
        }
    }
}

@Composable
fun InfoItem(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}