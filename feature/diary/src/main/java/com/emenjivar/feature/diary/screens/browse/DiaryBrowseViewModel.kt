package com.emenjivar.feature.diary.screens.browse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.data.repositories.DiaryEntryRepository
import com.emenjivar.feature.diary.navigation.ViewModelNavigation
import com.emenjivar.feature.diary.navigation.ViewModelNavigationImp
import com.emenjivar.feature.diary.screens.entry.DiaryEntryRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DiaryBrowseViewModel @Inject constructor(
    diaryEntryRepository: DiaryEntryRepository
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImp() {
    private val entries = diaryEntryRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private fun navigateToNewEntry() {
        navigate(DiaryEntryRoute)
    }

    private fun navigateToDetailEntry(uid: Long) {
        // TODO: add detail
        Log.wtf("DiaryBrowseViewModel", "uid: $uid")
    }

    val uiState = DiaryBrowseUiState(
        entries = entries,
        navigateToNewEntry = ::navigateToNewEntry,
        navigateToDetailEntry = ::navigateToDetailEntry
    )
}
