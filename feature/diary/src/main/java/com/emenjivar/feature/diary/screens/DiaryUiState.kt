package com.emenjivar.feature.diary.screens

data class DiaryUiState(
    val saveEntry: (text: String) -> Unit,
    val popBackStack: () -> Unit
)
