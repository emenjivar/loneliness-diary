package com.emenjivar.feature.diary.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.core.data.models.DiaryEntryEmotion
import com.emenjivar.core.data.repositories.DiaryEntryRepository
import com.emenjivar.core.data.repositories.EmotionsRepository
import com.emenjivar.feature.diary.navigation.ViewModelNavigation
import com.emenjivar.feature.diary.navigation.ViewModelNavigationImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryEntryViewModel @Inject constructor(
    private val diaryEntryRepository: DiaryEntryRepository,
    emotionsRepository: EmotionsRepository
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImp() {

    private val emotions = emotionsRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private fun saveEntry(text: String, insertedItems: List<InsertedItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            val entries = insertedItems.map {
                val emotion = it as InsertedItem.Emotion
                DiaryEntryEmotion(
                    emotion = emotion.text,
                    insertionIndex = emotion.startIndex
                )
            }


            diaryEntryRepository.insert(
                DiaryEntry(
                    title = "Mock title",
                    content = text,
                    entries = entries
                )
            )
            popBackStack()
        }
    }

    val uiState = DiaryEntryUiState(
        emotions = emotions,
        saveEntry = ::saveEntry,
        popBackStack = ::popBackStack
    )
}
