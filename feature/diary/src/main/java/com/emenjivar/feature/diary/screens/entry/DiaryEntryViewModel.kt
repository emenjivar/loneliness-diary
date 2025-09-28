package com.emenjivar.feature.diary.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.core.data.models.DiaryEntryEmotion
import com.emenjivar.core.data.models.EmotionData
import com.emenjivar.core.data.repositories.DiaryEntryRepository
import com.emenjivar.core.data.repositories.EmotionsRepository
import com.emenjivar.feature.diary.ext.stateInDefault
import com.emenjivar.feature.diary.navigation.ViewModelNavigation
import com.emenjivar.feature.diary.navigation.ViewModelNavigationImp
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DiaryEntryViewModel.Factory::class)
class DiaryEntryViewModel @AssistedInject constructor(
    private val diaryEntryRepository: DiaryEntryRepository,
    @Assisted private val id: Long,
    emotionsRepository: EmotionsRepository
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImp() {
    @AssistedFactory
    interface Factory {
        fun create(id: Long): DiaryEntryViewModel
    }

    private val emotions = emotionsRepository.getAll()
        .stateInDefault(
            scope = viewModelScope,
            initialValue = emptyList()
        )

    private val emotionsMap = emotions.map { emotions ->
        emotions.associateBy { it.name }
    }

    private val entry = diaryEntryRepository.findById(id)

    private val initialText = entry
        .filterNotNull()
        .mapNotNull { it.content }
        .stateInDefault(
            scope = viewModelScope,
            initialValue = ""
        )

    private val initialInsertions = combine(
        entry.filterNotNull(),
        emotionsMap
    ) { entry, emotionsMap ->
        entry.emotions.map { emotion ->
            InsertedItem.Emotion(
                data = emotionsMap[emotion.emotion] ?: EmotionData.empty,
                startIndex = emotion.insertionIndex
            )
        }
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = emptyList()
    )

    private fun saveEntry(
        text: String,
        insertedItems: List<InsertedItem>
    ) {
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
                    id = id,
                    title = "Mock title",
                    content = text,
                    emotions = entries
                )
            )
            popBackStack()
        }
    }

    val uiState = DiaryEntryUiState(
        emotions = emotions,
        initialText = initialText,
        initialInsertions = initialInsertions,
        saveEntry = ::saveEntry,
        popBackStack = ::popBackStack
    )
}
