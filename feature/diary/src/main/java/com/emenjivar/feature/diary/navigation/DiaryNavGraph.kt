package com.emenjivar.feature.diary.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.emenjivar.feature.diary.screens.browse.DiaryBrowseRoute
import com.emenjivar.feature.diary.screens.browse.DiaryBrowseScreen
import com.emenjivar.feature.diary.screens.entry.DiaryEntryContent
import com.emenjivar.feature.diary.screens.entry.DiaryEntryRoute

fun EntryProviderBuilder<NavKey>.featureGraph(
    onNavigateAction: (NavigationAction) -> Unit
) {
    entry<DiaryBrowseRoute> {
        DiaryBrowseScreen(onNavigateAction = onNavigateAction)
    }
    entry<DiaryEntryRoute> {
        DiaryEntryContent(onNavigateAction = onNavigateAction)
    }
}
