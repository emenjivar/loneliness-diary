package com.emenjivar.feature.diary.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.emenjivar.feature.diary.screens.DiaryScreen

fun EntryProviderBuilder<NavKey>.featureGraph(
    navigateTo: (NavKey) -> Unit,
    popBackStack: () -> Unit
) {
    entry<DiaryRoute> { DiaryScreen() }
}