package com.emenjivar.feature.diary.screens.entry.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Wrapper for [SheetState] that associated typed data with bottom sheet.
 *
 * Useful for passing context-specific data when showing a bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetStateWithData<T>(
    val sheetState: SheetState
) {
    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet

    private var _data: T? = null
    val data: T
        get() = checkNotNull(_data)

    val nullableData: T?
        get() = _data

    suspend fun expand(new: T) {
        _data = new
        _showBottomSheet.update { true }
        delay(10L) // TODO: prevent blinks when opening the shit
        sheetState.expand()
    }

    suspend fun hide() {
        sheetState.hide()
        _showBottomSheet.update { false }
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

/**
 * Creates and remember a [BottomSheetStateWithData] instance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberBottomSheetState(
    sheetState: SheetState = rememberModalBottomSheetState()
): BottomSheetStateWithData<Unit> {
    return remember(sheetState) {
        BottomSheetStateWithData(sheetState)
    }
}
