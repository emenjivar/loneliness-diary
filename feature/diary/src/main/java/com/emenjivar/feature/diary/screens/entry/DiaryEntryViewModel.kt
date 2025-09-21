package com.emenjivar.feature.diary.screens.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.data.models.DiaryEntry
import com.emenjivar.core.data.repositories.DiaryEntryRepository
import com.emenjivar.feature.diary.navigation.ViewModelNavigation
import com.emenjivar.feature.diary.navigation.ViewModelNavigationImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryEntryViewModel @Inject constructor(
    private val diaryEntryRepository: DiaryEntryRepository
) : ViewModel(), ViewModelNavigation by ViewModelNavigationImp() {
    fun saveEntry(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryEntryRepository.insert(
                DiaryEntry(
                    title = "Mock title",
                    content = text
                )
            )
            popBackStack()
        }
    }

    val uiState = DiaryEntryUiState(
        saveEntry = ::saveEntry,
        popBackStack = ::popBackStack
    )
}
