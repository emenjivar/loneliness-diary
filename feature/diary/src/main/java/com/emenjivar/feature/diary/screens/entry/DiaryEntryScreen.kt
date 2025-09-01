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
                modifier = Modifier.focusRequester(focusRequester),
                value = textFieldValue.value,
                onValueChange = { updatedValue ->
                    // Rebuilt the annotatedString preserving insertion styles
                    val newAnnotatedString = applyStylesToAnnotatedString(
                        rawText = updatedValue.text,
                        insertions = insertions
                    )

                    textFieldValue.value = updatedValue.copy(annotatedString = newAnnotatedString)
                }
            )


            Button(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .imePadding(),
                onClick = {
                    val originalTextField = textFieldValue.value
                    val emotion: InsertedItem = InsertedItem.Emotion(
                        data = listOf(Sad, Angry).random(),
                        startIndex = originalTextField.selection.start
                    )

                    insertions.add(emotion)

                    textFieldValue.value = originalTextField.copy(
                        annotatedString = buildInsertAnnotatedString(
                            annotatedString = originalTextField.annotatedString,
                            item = emotion
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
