package com.emenjivar.feature.diary.screens.entry

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class DiaryEntryRoute(val id: Long = 0L) : NavKey
