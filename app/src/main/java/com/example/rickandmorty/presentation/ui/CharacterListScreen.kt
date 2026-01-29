package com.example.rickandmorty.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.CharactersStatus
import com.example.rickandmorty.presentation.viewmodel.CharactersListIntent
import com.example.rickandmorty.presentation.viewmodel.CharactersListState
import com.example.rickandmorty.presentation.viewmodel.CharactersListViewModel

@Composable
fun CharactersListScreen(
    onCharacterClick: (Long) -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: CharactersListViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val searchQuery = viewModel.searchQuery.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Строка поиска с кнопкой уведомлений
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.TextField(
                value = searchQuery,
                onValueChange = { query ->
                    viewModel.processIntent(CharactersListIntent.Search(query))
                },
                placeholder = { Text("Поиск персонажей...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Gray
                ),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Кнопка перехода к тесту WorkManager
            IconButton(
                onClick = onNotificationClick
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Тест уведомлений",
                    tint = Color.Black
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is CharactersListState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color.Black
                    )
                }

                is CharactersListState.Empty -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ничего не найдено",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.processIntent(CharactersListIntent.LoadFirstPage)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text("Обновить", color = Color.White)
                        }
                    }
                }

                is CharactersListState.Success -> {
                    val successState = state as CharactersListState.Success
                    CharactersList(
                        characters = successState.characters,
                        hasNextPage = successState.hasNextPage,
                        onCharacterClick = onCharacterClick,
                        onFavoriteClick = { characterId ->
                            viewModel.processIntent(
                                CharactersListIntent.ToggleFavorite(characterId)
                            )
                        },
                        onLoadMore = {
                            viewModel.processIntent(CharactersListIntent.LoadNextPage)
                        }
                    )
                }

                is CharactersListState.Error -> {
                    val errorState = state as CharactersListState.Error
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorState.message,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.processIntent(CharactersListIntent.LoadFirstPage)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text("Повторить", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharactersList(
    characters: List<AllCharacterDomainModel>,
    hasNextPage: Boolean,
    onCharacterClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onLoadMore: () -> Unit
) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(characters) { character ->
            CharacterItem(
                character = character,
                onClick = { onCharacterClick(character.id) },
                onFavoriteClick = { onFavoriteClick(character.id) },
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Divider(color = Color.LightGray, thickness = 0.5.dp)
        }

        if (hasNextPage) {
            item {
                Button(
                    onClick = onLoadMore,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("Загрузить еще")
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: AllCharacterDomainModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = character.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when (character.status) {
                                CharactersStatus.ALIVE -> Color.Green
                                CharactersStatus.DEAD -> Color.Red
                                CharactersStatus.UNKNOWN -> Color.Gray
                            }
                        )
                )

                Text(
                    text = "${character.status.name} - ${character.species}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = if (character.isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Outlined.Favorite
                },
                contentDescription = "Избранное",
                tint = if (character.isFavorite) Color.Red else Color.Gray
            )
        }
    }
}