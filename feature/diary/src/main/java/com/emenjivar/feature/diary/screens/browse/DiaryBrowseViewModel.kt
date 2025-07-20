package com.emenjivar.feature.diary.screens.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.data.repositories.DiaryEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DiaryBrowseViewModel @Inject constructor(
    diaryEntryRepository: DiaryEntryRepository
) : ViewModel() {

    private val entries = diaryEntryRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val uiState = DiaryBrowseUiState(
        entries = entries,
        navigateToNewEntry = {} // TODO: navigate using  material3
    )
}
