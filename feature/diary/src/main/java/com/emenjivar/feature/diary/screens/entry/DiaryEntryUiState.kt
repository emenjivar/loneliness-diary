package com.emenjivar.feature.diary.screens.entry

data class DiaryEntryUiState(
    val saveEntry: (text: String, insertions: List<InsertedItem>) -> Unit,
    val popBackStack: () -> Unit
)
