package com.emenjivar.feature.diary.screens.entry

import com.emenjivar.core.data.models.EmotionData
import com.emenjivar.core.data.models.SongModel
import com.emenjivar.core.data.utils.ResultWrapper
import kotlinx.coroutines.flow.StateFlow

data class DiaryEntryUiState(
    val emotions: StateFlow<List<EmotionData>>,
    val initialText: StateFlow<String>,
    val initialInsertions: StateFlow<List<InsertedItem>>,
    val search: StateFlow<String>,
    // TODO: add recent songs here
    val searchedSongs: StateFlow<ResultWrapper<List<SongModel>>>,
    val saveEntry: (text: String, insertions: List<InsertedItem>) -> Unit,
    val onSearchSong: (String) -> Unit,
    val popBackStack: () -> Unit
)
