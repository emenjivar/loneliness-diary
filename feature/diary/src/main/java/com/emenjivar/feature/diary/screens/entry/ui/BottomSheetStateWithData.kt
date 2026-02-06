package com.emenjivar.feature.diary.screens.entry.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Wrapper for [SheetState] that associated typed data with bottom sheet.
 *
 * Useful for passing context-specific data when showing a bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetStateWithData<T>(
    val sheetState: SheetState
) {
    private var _data: T? = null
    val data: T
        get() = checkNotNull(_data)

    val nullableData: T?
        get() = _data

    suspend fun expand(new: T) {
        _data = new
        sheetState.expand()
    }

    suspend fun hide() {
        sheetState.hide()
        _data = null
    }
}

/**
 * Creates and remember a [BottomSheetStateWithData] instance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> rememberBottomSheetStateWithData(
    sheetState: SheetState = rememberModalBottomSheetState()
): BottomSheetStateWithData<T> {
    return remember(sheetState) {
        BottomSheetStateWithData(sheetState)
    }
}
