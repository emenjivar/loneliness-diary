package com.emenjivar.feature.diary.screens.browse

import com.emenjivar.core.data.models.DiaryEntry
import kotlinx.coroutines.flow.StateFlow

data class DiaryBrowseUiState(
    val entries: StateFlow<List<DiaryEntry>>,
    val navigateToNewEntry: () -> Unit,
    val navigateToDetailEntry: (uid: Int) -> Unit
)
