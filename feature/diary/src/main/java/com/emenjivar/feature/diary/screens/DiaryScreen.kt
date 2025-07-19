package com.emenjivar.feature.diary.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emenjivar.feature.diary.ui.InsertOption
import com.emenjivar.feature.diary.ui.InsertOptionMenu
import kotlinx.coroutines.delay

@Composable
internal fun DiaryScreen(
    viewModel: DiaryScreenViewModel = hiltViewModel()
) {
    DiaryScreen(uiState = viewModel.uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DiaryScreen(
    uiState: DiaryUiState
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }
    var showMenu by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
//    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var menuOffset by remember { mutableStateOf(Offset.Zero) }

//    val windowInsets = WindowInsets.ime
    val density = LocalDensity.current
//    val keyboardHeight = with(density) {
//        windowInsets.getBottom(density = LocalDensity.current).toDp()
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = uiState.popBackStack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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
                .padding(horizontal = 20.dp)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue

                    if (newValue.text.lastOrNull() == '/') {
                        showMenu = true

                        textLayoutResult?.let { layout ->
                            val cursorRect = layout.getCursorRect(newValue.selection.start - 1)
                            menuOffset = Offset(
                                x = cursorRect.left,
                                y = cursorRect.bottom
                            )
                        }
                    } else {
                        showMenu = false
                    }
                },
                onTextLayout = {
                    textLayoutResult = it
                },
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.TopStart) {
                        if (textFieldValue.text.isEmpty()) {
                            Text(
                                text = "Type something...",
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (showMenu) {
                InsertOptionMenu(
                    modifier = Modifier.graphicsLayer {
                        translationY = menuOffset.y
                    },
                    onSelect = { action ->
                        showMenu = false
                        val textWithoutSlash = textFieldValue.text.dropLast(1)

                        // TODO: should open a bottom dialog instead
                        when (action) {
                            InsertOption.EMOTION -> {
                                val updatedText = "$textWithoutSlash[emotion:sad]"
                                textFieldValue = textFieldValue.copy(
                                    text = updatedText,
                                    selection = TextRange(updatedText.length)
                                )
                            }

                            InsertOption.COLOR -> {
                                val updatedText = "$textWithoutSlash[color:blue]"
                                textFieldValue = textFieldValue.copy(
                                    text = updatedText,
                                    selection = TextRange(updatedText.length)
                                )
                            }

                            InsertOption.SONG -> {
                                val updatedText = "$textWithoutSlash[song:https://mysong.co/23987]"
                                textFieldValue = textFieldValue.copy(
                                    text = updatedText,
                                    selection = TextRange(updatedText.length)
                                )
                            }

                            InsertOption.PLACE -> {
                                val updatedText = "$textWithoutSlash[place:1223,2323]"
                                textFieldValue = textFieldValue.copy(
                                    text = updatedText,
                                    selection = TextRange(updatedText.length)
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(500)
        focusRequester.requestFocus()
    }
}
