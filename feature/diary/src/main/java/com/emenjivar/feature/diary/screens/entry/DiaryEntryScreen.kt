package com.emenjivar.feature.diary.screens.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emenjivar.feature.diary.navigation.HandleNavigation
import com.emenjivar.feature.diary.navigation.NavigationAction
import kotlinx.coroutines.delay

@Composable
internal fun DiaryEntryScreen(
    viewModel: DiaryEntryViewModel = hiltViewModel(),
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
    val focusRequester = remember { FocusRequester() }
    val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
    val insertions = remember { mutableStateListOf<InsertedItem>() }

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
                    .padding(20.dp), // TODO: add dimens file here
                value = textFieldValue.value,
                onValueChange = { updatedValue ->
                    val isDeleting = updatedValue.text.length < textFieldValue.value.text.length
                    val isAdding = updatedValue.text.length > textFieldValue.value.text.length
                    val cursorIndex = updatedValue.selection.start

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
                                val stringAfterInsertedText =
                                    textFieldValue.value.text.substring(startIndex = cursorInsertedItem.startIndex + cursorInsertedItem.length)
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
                                    val data = insertion as InsertedItem.Emotion
                                    insertions[index] = data.copy(
                                        // Shift the insertions by the number of characters of the deleted insertion
                                        // Or just shift by 1 (assuming there's no multi selection)
                                        startIndex = data.startIndex - (cursorInsertedItem?.length
                                            ?: 1)
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
                                    val data = insertion as InsertedItem.Emotion
                                    insertions[index] = data.copy(
                                        startIndex = data.startIndex + (updatedValue.selection.length + 1)
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
                        }
                    }
                }
            )


            Button(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .imePadding(),
                onClick = {
                    if (shouldBlockInsertion(
                            selection = textFieldValue.value.selection,
                            insertions = insertions
                        )
                    ) {
                        return@Button
                    }

                    val originalTextField = textFieldValue.value
                    val emotion: InsertedItem = InsertedItem.Emotion(
                        data = listOf(Sad).random(),
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
                }) {
                Text("Insert")
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(DELAY_FOCUS)
        focusRequester.requestFocus()
    }
}

private const val DELAY_FOCUS = 500L
