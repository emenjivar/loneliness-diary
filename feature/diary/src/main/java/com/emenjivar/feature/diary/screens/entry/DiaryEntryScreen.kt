@file:Suppress("CyclomaticComplexMethod", "MaxLineLength")
package com.emenjivar.feature.diary.screens.entry

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emenjivar.core.data.models.EmotionData
import com.emenjivar.core.data.utils.ResultWrapper
import com.emenjivar.feature.diary.navigation.HandleNavigation
import com.emenjivar.feature.diary.navigation.NavigationAction
import com.emenjivar.feature.diary.screens.entry.ui.EmotionViewBottomSheet
import com.emenjivar.feature.diary.screens.entry.ui.EmotionsBottomSheet
import com.emenjivar.feature.diary.screens.entry.ui.MusicBottomSheet
import com.emenjivar.feature.diary.screens.entry.ui.rememberBottomSheetStateWithData
import com.emenjivar.feature.diary.util.DELAY_FOCUS
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @param route Route with navigation arguments.
 */
@Composable
internal fun DiaryEntryScreen(
    route: DiaryEntryRoute,
    viewModel: DiaryEntryViewModel = hiltViewModel<DiaryEntryViewModel, DiaryEntryViewModel.Factory>(
        creationCallback = { factory -> factory.create(route) }
    ),
    onNavigateAction: (NavigationAction) -> Unit
) {
    DiaryEntryScreen(uiState = viewModel.uiState)
    HandleNavigation(
        navigationFlow = viewModel.navigationFlow,
        onNavigateAction = onNavigateAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryEntryScreen(
    uiState: DiaryEntryUiState
) {
    val emotions by uiState.emotions.collectAsStateWithLifecycle()
    val initialText by uiState.initialText.collectAsStateWithLifecycle()
    val initialInsertions by uiState.initialInsertions.collectAsStateWithLifecycle()
    val search by uiState.search.collectAsStateWithLifecycle()
    val searchedSongs by uiState.searchedSongs.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    val textFieldValue = remember(initialText) {
        mutableStateOf(
            TextFieldValue(
                text = initialText,
                selection = TextRange(initialText.length.coerceAtLeast(0))
            )
        )
    }
    val insertions = remember(initialInsertions) {
        mutableStateListOf<InsertedItem>().apply {
            addAll(initialInsertions)
        }
    }
    val isSaveEnabled by remember(initialText) {
        derivedStateOf { textFieldValue.value.text.isNotBlank() }
    }
    val emotionsSheetState = rememberModalBottomSheetState()
    val musicSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val emotionViewSheetState = rememberBottomSheetStateWithData<EmotionData>()
    val coroutineScope = rememberCoroutineScope()
    val localKeyboard = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = uiState.popBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            // TODO: use an string resource here
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        enabled = isSaveEnabled,
                        onClick = { uiState.saveEntry(textFieldValue.value.text, insertions) }
                    ) {
                        Text(text = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            BasicTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    // TODO: add dimens file here
                    .padding(horizontal = 20.dp),
                value = textFieldValue.value,
                onValueChange = { updatedValue ->
                    val isDeleting = updatedValue.text.length < textFieldValue.value.text.length
                    val isAdding = updatedValue.text.length > textFieldValue.value.text.length
                    val cursorIndex = updatedValue.selection.start

                    Log.wtf("DiaryEntryScreen", "selection: $updatedValue")
                    Log.wtf("DiaryEntryScreen", "insertions: $insertions")
                    val itemSelected = insertions.find {
                        updatedValue.selection.start >= it.startIndex &&
                            updatedValue.selection.end <= (it.startIndex + it.length)
                    }
                    // TODO: this is the clicked insertion, this should be loaded somehow in the UI
                    Log.wtf("DiaryEntryScreen", "itemSelected: $itemSelected")

                    when {
                        isDeleting -> {
                            // Insertion selected by the cursor pointer
                            val cursorInsertedItem = insertions.firstOrNull {
                                cursorIndex in it.startIndex..<it.startIndex + it.length
                            }

                            var textFieldDeletion: TextFieldValue = updatedValue
                            if (cursorInsertedItem != null) {
                                // Removes the style of the partially deleted insertion
                                insertions.remove(cursorInsertedItem)

                                // Deletes an inserted word when any of its characters is deleted
                                val stringBeforeInsertedText = textFieldValue.value.text.substring(
                                    startIndex = 0,
                                    endIndex = cursorInsertedItem.startIndex
                                )
                                val stringAfterInsertedText = textFieldValue.value.text.substring(
                                    startIndex = cursorInsertedItem.startIndex + cursorInsertedItem.length
                                )
                                val stringWithoutInsertedText =
                                    stringBeforeInsertedText + stringAfterInsertedText
                                textFieldDeletion = textFieldValue.value.copy(
                                    text = stringWithoutInsertedText,
                                    selection = TextRange(cursorInsertedItem.startIndex)
                                )
                            }

                            // Apply a negative offset to the insertions ahead the cursor
                            insertions.forEachIndexed { index, insertion ->
                                if (insertion.startIndex + insertion.length > cursorIndex) {
                                    insertions[index] = insertion.updateStartIndex(
                                        // Shift the insertions by the number of characters of the deleted insertion
                                        // Or just shift by 1 (assuming there's no multi selection)
                                        newStartIndex = insertion.startIndex - (cursorInsertedItem?.length ?: 1)
                                    )
                                }
                            }

                            // Rebuilt the annotatedString preserving insertion styles
                            val newAnnotatedString = applyStylesToAnnotatedString(
                                rawText = textFieldDeletion.text,
                                insertions = insertions
                            )
                            textFieldValue.value =
                                textFieldDeletion.copy(annotatedString = newAnnotatedString)
                        }

                        isAdding -> {
                            // Preventing text edition on an already existent insertion,
                            // an inserted word can only be deleted
                            val insertedItemWithinRange = insertions.firstOrNull {
                                // when a new character is added to the string, the cursor increases +1
                                // that's why i subtract 1
                                (cursorIndex - 1) in (it.startIndex + 1)..<(it.startIndex + it.length)
                            }
                            if (insertedItemWithinRange != null) {
                                return@BasicTextField
                            }

                            // Keep previous insertions (before the cursor)
                            val previousInsertions =
                                insertions.filter { it.startIndex + it.length < cursorIndex }

                            // Apply a positive offset of the insertion ahead the cursor
                            insertions.forEachIndexed { index, insertion ->
                                if (insertion.startIndex + insertion.length >= cursorIndex) {
                                    insertions[index] = insertion.updateStartIndex(
                                        newStartIndex = insertion.startIndex + (updatedValue.selection.length + 1)
                                    )
                                }
                            }

                            // Rebuilt the annotatedString preserving insertion styles
                            val newAnnotatedString = applyStylesToAnnotatedString(
                                rawText = updatedValue.text,
                                insertions = previousInsertions + insertions
                            )
                            textFieldValue.value =
                                updatedValue.copy(annotatedString = newAnnotatedString)
                        }

                        else -> {
                            // Rebuilt the annotatedString preserving insertion styles
                            val newAnnotatedString = applyStylesToAnnotatedString(
                                rawText = updatedValue.text,
                                insertions = insertions
                            )
                            textFieldValue.value = updatedValue.copy(
                                annotatedString = newAnnotatedString
                            )

//                            // Open the respective bottomSheet when the cursor is within the range of the insertion
//                            val selectedInsertion = insertions.firstOrNull { insertion ->
//                                val startBound = updatedValue.selection.start > insertion.startIndex
//                                val endBound = updatedValue.selection.start < insertion.startIndex + insertion.length - 1
//                                startBound && endBound
//                            }
//
//                            // Click/tap over the rich text insertion
//                            if (selectedInsertion != null){
//                                localKeyboard?.hide()
//                                focusRequester.freeFocus()
//
//                                when (selectedInsertion) {
//                                    is InsertedItem.Emotion -> {
//                                        coroutineScope.launch {
//                                            //delay(500)
//                                            emotionViewSheetState.expand(selectedInsertion.data)
//                                        }
//                                    }
//                                    is InsertedItem.Song -> {}
//                                }
//                            }
                        }
                    }
                }
            )

            HorizontalActions(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .imePadding()
            ) { action ->
                when (action) {
                    DiaryEntryAction.EMOTION -> {
                        coroutineScope.launch {
                            localKeyboard?.hide()
                            emotionsSheetState.show()
                        }
                    }
                    DiaryEntryAction.MUSIC -> {
                        coroutineScope.launch {
                            musicSheetState.expand()
                        }
                    }
                }
            }
        }
    }

    EmotionsBottomSheet(
        sheetState = emotionsSheetState,
        emotions = emotions,
        onEmotionClick = { selectedEmotion ->
            coroutineScope.launch {
                emotionsSheetState.hide()
                if (shouldBlockInsertion(
                        selection = textFieldValue.value.selection,
                        insertions = insertions
                    )
                ) {
                    return@launch
                }

                val originalTextField = textFieldValue.value
                val emotion: InsertedItem = InsertedItem.Emotion(
                    data = selectedEmotion,
                    startIndex = originalTextField.selection.start
                )

                insertItem(
                    insertions = insertions,
                    newElement = emotion
                )

                val updatedText = insertText(
                    original = originalTextField.text,
                    newElement = emotion
                )

                textFieldValue.value = originalTextField.copy(
                    annotatedString = applyStylesToAnnotatedString(
                        rawText = updatedText,
                        insertions = insertions
                    ),
                    // Put the cursor at the end of the inserted word
                    selection = TextRange(
                        originalTextField.selection.end + emotion.length
                    )
                )

                localKeyboard?.show()
            }
        }
    )

    MusicBottomSheet(
        sheetState = musicSheetState,
        songs = searchedSongs,
        recentSongs = emptyList(),
        search = search,
        onSearchSong = uiState.onSearchSong,
        onTriggerImmediateSearch = uiState.onTriggerImmediateSearch,
        onClickSong = { song ->
            coroutineScope.launch {
                musicSheetState.hide()

                if (
                    shouldBlockInsertion(
                        selection = textFieldValue.value.selection,
                        insertions = insertions
                    )
                ) {
                    // TODO: just append the song at the end of the text
                    //  To prevent another API search
                    return@launch
                }

                val originalTextField = textFieldValue.value
                val song: InsertedItem = InsertedItem.Song(
                    data = song,
                    startIndex = originalTextField.selection.start
                )

                insertItem(
                    insertions = insertions,
                    newElement = song
                )

                val updatedText = insertText(
                    original = originalTextField.text,
                    newElement = song
                )

                textFieldValue.value = originalTextField.copy(
                    annotatedString = applyStylesToAnnotatedString(
                        rawText = updatedText,
                        insertions = insertions
                    ),
                    // Put the cursor at the end of the inserted word
                    selection = TextRange(
                        originalTextField.selection.end + song.length
                    )
                )

                localKeyboard?.show()
            }
        }
    )

    EmotionViewBottomSheet(
        sheetState = emotionViewSheetState
    )

    LaunchedEffect(Unit) {
        delay(DELAY_FOCUS)
        focusRequester.requestFocus()
    }
}

@Composable
private fun HorizontalActions(
    modifier: Modifier = Modifier,
    onClick: (DiaryEntryAction) -> Unit
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        DiaryEntryAction.entries.forEach { action ->
            AssistChip(
                modifier = Modifier.semantics {
                    contentDescription = action.contentDescription
                },
                onClick = {
                    onClick(action)
                },
                label = {
                    Text(text = action.title)
                }
            )
        }
    }
}

@Preview
@Composable
private fun DiaryEntryScreenPreview() {
    DiaryEntryScreen(
        uiState = DiaryEntryUiState(
            emotions = MutableStateFlow(emptyList()),
            initialText = MutableStateFlow(""),
            initialInsertions = MutableStateFlow(emptyList()),
            search = MutableStateFlow(""),
            searchedSongs = MutableStateFlow(ResultWrapper.Loading),
            saveEntry = { _, _ -> },
            onSearchSong = {},
            onTriggerImmediateSearch = {},
            popBackStack = {}
        )
    )
}

@Preview
@Composable
private fun DiaryEntryScreenWithDataPreview() {
    DiaryEntryScreen(
        uiState = DiaryEntryUiState(
            emotions = MutableStateFlow(emptyList()),
            initialText = MutableStateFlow("Today i feel sad"),
            initialInsertions = MutableStateFlow(
                listOf(
                    InsertedItem.Emotion(
                        data = EmotionData(
                            name = "sad",
                            color = 0xff0d47a1,
                            description = ""
                        ),
                        startIndex = 13
                    )
                )
            ),
            search = MutableStateFlow(""),
            searchedSongs = MutableStateFlow(ResultWrapper.Loading),
            saveEntry = { _, _ -> },
            onSearchSong = {},
            onTriggerImmediateSearch = {},
            popBackStack = {}
        )
    )
}
