package com.emenjivar.feature.diary.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiaryScreenViewModel @Inject constructor() : ViewModel() {
    val uiState = DiaryUiState(
        popBackStack = {} // TODO: implement the navigation logic for the viewModels
    )
}
