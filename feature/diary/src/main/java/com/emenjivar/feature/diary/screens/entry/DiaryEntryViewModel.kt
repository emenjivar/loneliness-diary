package com.emenjivar.feature.diary.screens.entry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.core.data.models.DiaryEntryEmotion
import com.emenjivar.core.data.models.EmotionData
import com.emenjivar.core.data.repositories.DiaryEntryRepository
import com.emenjivar.core.data.repositories.EmotionsRepository
import com.emenjivar.core.data.repositories.SongsRepository
import com.emenjivar.core.data.utils.ResultWrapper
import com.emenjivar.feature.diary.ext.stateInDefault
import com.emenjivar.feature.diary.navigation.ViewModelNavigation
import com.emenjivar.feature.diary.navigation.ViewModelNavigationImp
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DiaryEntryViewModel.Factory::class)
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class DiaryEntryViewModel @AssistedInject constructor(
    private val diaryEntryRepository: DiaryEntryRepository,
    private val songsRepository: SongsRepository,
    @Assisted private val route: DiaryEntryRoute,
    emotionsRepository: EmotionsRepository
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImp() {
    @AssistedFactory
    interface Factory {
        fun create(route: DiaryEntryRoute): DiaryEntryViewModel
    }

    private val emotions = emotionsRepository.getAll()
        .stateInDefault(
            scope = viewModelScope,
            initialValue = emptyList()
        )

    private val emotionsMap = emotions.map { emotions ->
        emotions.associateBy { it.name }
    }

    private val entry = diaryEntryRepository.findById(route.id)

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

    private val search = MutableStateFlow("")
    private val debounceSearch = search
        .filter { it.trim().isNotBlank() }
        .debounce(1000)

    private val searchTrigger = MutableSharedFlow<Unit>(replay = 0)
    private val immediateSearch = searchTrigger
        .map { search.value }
        .filter { it.trim().isNotBlank() }

    private val searchedSongs = merge(debounceSearch, immediateSearch)
        .distinctUntilChanged()
        .flatMapLatest { search ->
            songsRepository.search(query = search, limit = 10)
        }.stateInDefault(
            scope = viewModelScope,
            initialValue = ResultWrapper.Success(emptyList())
        )

    val uiState = DiaryEntryUiState(
        emotions = emotions,
        initialText = initialText,
        initialInsertions = initialInsertions,
        search = search,
        searchedSongs = searchedSongs,
        saveEntry = ::saveEntry,
        onSearchSong = ::onSearchSong,
        onTriggerImmediateSearch = ::onTriggerImmediateSearch,
        popBackStack = ::popBackStack
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
                    id = route.id,
                    title = "Mock title",
                    content = text,
                    emotions = entries
                )
            )
            popBackStack()
        }
    }

    private fun onSearchSong(value: String) {
        search.update { value }
    }

    private fun onTriggerImmediateSearch() {
        viewModelScope.launch {
            searchTrigger.emit(Unit)
        }
    }
}
