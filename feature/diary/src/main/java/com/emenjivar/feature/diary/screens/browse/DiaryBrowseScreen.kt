package com.emenjivar.feature.diary.screens.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.feature.diary.navigation.HandleNavigation
import com.emenjivar.feature.diary.navigation.NavigationAction

@Composable
internal fun DiaryBrowseScreen(
    viewModel: DiaryBrowseViewModel = hiltViewModel(),
    onNavigateAction: (NavigationAction) -> Unit
) {
    DiaryBrowseContent(uiState = viewModel.uiState)
    HandleNavigation(
        navigationFlow = viewModel.navigationFlow,
        onNavigateAction = onNavigateAction
    )
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
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                contentPadding = PaddingValues(bottom = 50.dp, start = 12.dp, end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = entries,
                    key = { entry -> entry.id }
                ) { entry ->
                    DiaryEntry(
                        entry = entry,
                        onClick = {
                            uiState.navigateToDetailEntry(entry.id)
                        }
                    )
                }
            }

            Button(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            bottom = paddingValues.calculateBottomPadding(),
                            end = 12.dp
                        ),
                onClick = uiState.navigateToNewEntry
            ) {
                Text("New entry")
            }
        }
    }
}

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
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "createdAt: ${entry.createdAt}",
            fontSize = 10.sp
        )
        if (entry.updatedAt != null) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "createdAt: ${entry.updatedAt}",
                fontSize = 10.sp
            )
        }
    }
}
