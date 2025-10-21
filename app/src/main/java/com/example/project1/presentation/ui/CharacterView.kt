package com.example.project1.presentation.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterList
import coil.compose.AsyncImage
import com.example.project1.domain.entity.CharacterEntity
import com.example.project1.presentation.viewmodel.CharacterViewModel
import org.koin.androidx.compose.koinViewModel

import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit = {},
) {
    val viewModel: CharacterViewModel = koinViewModel()
    val characters by viewModel.characters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isError by viewModel.isError.collectAsState()

    val searchQuery by viewModel.searchQuery
    val selectedStatus by viewModel.selectedStatus
    val selectedGender by viewModel.selectedGender
    val selectedSpecies by viewModel.selectedSpecies

    var showFilters by remember { mutableStateOf(false) }

    val isRefreshing by viewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    LaunchedEffect(Unit) {
        viewModel.loadCharacter()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick and Morty") },
                actions = {
                    IconButton(onClick = { showFilters = true }) {
                        Icon(Icons.Filled.FilterList, "Фильтры")
                    }
                }
            )
        },
            content = { innerPadding ->
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.loadCharacter(true) },

                ){
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SearchTextField(
                            query = searchQuery,
                            onQueryChange = { viewModel.setSearchQuery(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        )

                        ActiveFiltersView(viewModel)

                        when {
                            isLoading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            isError -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Ошибка загрузки данных",
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    CharactersGrid(
                                        characters = characters,
                                        onCharacterClick = onCharacterClick
                                    )
                                }
                            }

                            characters.isEmpty() -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        if (searchQuery.isNotEmpty() || selectedStatus != null || selectedGender != null || selectedSpecies != null)
                                            "По вашему запросу ничего не найдено"
                                        else
                                            "Персонажей не найдено"
                                    )
                                }
                            }

                            else -> {
                                CharactersGrid(
                                    characters = characters,
                                    onCharacterClick = onCharacterClick
                                )
                            }
                        }
                    }
                }
            }
        }
    )

    if (showFilters) {
        FilterDialog(
            viewModel = viewModel,
            onDismiss = { showFilters = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActiveFiltersView(viewModel: CharacterViewModel) {
    val selectedStatus by viewModel.selectedStatus
    val selectedGender by viewModel.selectedGender
    val selectedSpecies by viewModel.selectedSpecies
    val searchQuery by viewModel.searchQuery

    val hasActiveFilters = selectedStatus != null || selectedGender != null || selectedSpecies != null || searchQuery.isNotEmpty()

    if (hasActiveFilters) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            searchQuery.takeIf { it.isNotEmpty() }?.let { query ->
                FilterChip(
                    selected = true,
                    onClick = { viewModel.setSearchQuery("")  },
                    label = { Text("Поиск: $query") }
                )
            }

            selectedStatus?.let { status ->
                FilterChip(
                    selected = true,
                    onClick = { viewModel.setStatusFilter(null) },
                    label = { Text(status) }
                )
            }

            selectedGender?.let { gender ->
                FilterChip(
                    selected = true,
                    onClick = { viewModel.setGenderFilter(null) },
                    label = { Text(gender) }
                )
            }

            selectedSpecies?.let { species ->
                FilterChip(
                    selected = true,
                    onClick = { viewModel.setSpeciesFilter(null) },
                    label = { Text(species) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    viewModel: CharacterViewModel,
    onDismiss: () -> Unit
) {
    val statusFilter by viewModel.selectedStatus
    val genderFilter by viewModel.selectedGender
    val speciesFilter by viewModel.selectedSpecies

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Фильтры") },
        text = {
            Column {
                Text("Статус:", style = MaterialTheme.typography.bodyMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Alive", "Dead", "unknown").forEach { status ->
                        FilterChip(
                            selected = statusFilter == status,
                            onClick = {
                                viewModel.setStatusFilter(if (statusFilter == status) null else status)
                            },
                            label = { Text(
                                status,
                                maxLines = 1
                            ) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Гендер:", style = MaterialTheme.typography.bodyMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Male", "Female", "Genderless", "unknown").forEach { gender
                        ->
                        FilterChip(
                            selected = genderFilter == gender,
                            onClick = {
                                viewModel.setGenderFilter(if (genderFilter == gender) null else gender)
                            },
                            label = {
                                Text(
                                    gender,
                                    maxLines = 1,
                            )},
                        )
                    }
                }
                Text("Вид:", style = MaterialTheme.typography.bodyMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Human", "Alien", "Humanoid", "Robot", "Animal", "Mythological", "unknown").forEach { species ->
                        FilterChip(
                            selected = speciesFilter == species,
                            onClick = {
                                viewModel.setSpeciesFilter(if (speciesFilter == species) null else species)
                            },
                            label = {
                                Text(
                                    species,
                                    maxLines = 1
                                )
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Применить")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.clearAllFilters()
                viewModel.loadCharacter()
                onDismiss()
            }) {
                Text("Сбросить")
            }
        }
    )
}


@Composable
fun CharactersGrid(
    characters: List<CharacterEntity>,
    onCharacterClick: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val rows = characters.chunked(2)

        items(rows) { rowCharacters ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CharacterCard(
                    character = rowCharacters[0],
                    modifier = Modifier.weight(1f),
                    onClick = { onCharacterClick(rowCharacters[0].id) }
                )

                if (rowCharacters.size >= 2) {
                    CharacterCard(
                        character = rowCharacters[1],
                        modifier = Modifier.weight(1f),
                        onClick = { onCharacterClick(rowCharacters[1].id) }
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: CharacterEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = "Картинка ${character.name}",
                modifier = Modifier
                    .size(120.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Column {
                Text(
                    text = "${character.status} | ${character.species} | ${character.gender}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Поиск персонажей...") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Поиск"
            )
        },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}