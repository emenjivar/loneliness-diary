package com.emenjivar.feature.diary.screens.entry

import com.emenjivar.core.data.models.EmotionData
import kotlinx.coroutines.flow.StateFlow

data class DiaryEntryUiState(
    val emotions: StateFlow<List<EmotionData>>,
    val initialText: StateFlow<String>,
    val initialInsertions: StateFlow<List<InsertedItem>>,
    val saveEntry: (text: String, insertions: List<InsertedItem>) -> Unit,
    val popBackStack: () -> Unit
)
