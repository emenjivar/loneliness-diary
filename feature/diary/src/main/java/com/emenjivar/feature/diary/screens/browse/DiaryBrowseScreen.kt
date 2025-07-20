package com.emenjivar.feature.diary.screens.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emenjivar.core.data.models.DiaryEntry

@Composable
internal fun DiaryBrowseScreen(
    viewModel: DiaryBrowseViewModel = hiltViewModel()
) {
    DiaryBrowseContent(uiState = viewModel.uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryBrowseContent(
    uiState: DiaryBrowseUiState
) {
    val entries by uiState.entries.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Loneliness diary")
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            contentPadding = PaddingValues(bottom = 50.dp, start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = entries,
                key = { entry -> entry.uid }
            ) { entry ->
                DiaryEntry(
                    entry = entry,
                    onClick = {
                        uiState.navigateToNewEntry(entry.uid)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Stable
@Composable
private fun DiaryEntry(
    entry: DiaryEntry,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = entry.content
        )
    }
}
