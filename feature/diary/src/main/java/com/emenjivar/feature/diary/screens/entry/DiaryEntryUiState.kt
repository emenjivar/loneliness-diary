package com.emenjivar.feature.diary.screens.entry

data class DiaryEntryUiState(
    val saveEntry: (text: String) -> Unit,
    val popBackStack: () -> Unit
)
