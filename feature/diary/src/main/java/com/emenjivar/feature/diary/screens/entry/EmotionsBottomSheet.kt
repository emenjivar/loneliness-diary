package com.emenjivar.feature.diary.screens.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionsBottomSheet(
    sheetState: SheetState,
    emotions: List<EmotionData>,
    onEmotionClick: (EmotionData) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                }
            }
        ){
            EmotionBottomSheetLayout(
                emotions = emotions,
                onClick = onEmotionClick
            )
        }
    }
}

@Composable
@Stable
private fun EmotionBottomSheetLayout(
    emotions: List<EmotionData>,
    modifier: Modifier = Modifier,
    onClick: (EmotionData) -> Unit
) {
    Card(modifier = modifier) {
        LazyColumn {
            items(emotions) { emotion ->
                EmotionItem(
                    modifier = Modifier.clickable { onClick(emotion) },
                    emotion = emotion
                )
            }
        }
    }
}

@Composable
@Stable
private fun EmotionItem(
    emotion: EmotionData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(
            modifier = modifier
                .fillMaxHeight()
                .width(5.dp)
                .background(emotion.color)
        )
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Text(
                text = emotion.name,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = emotion.description,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview
@Composable
private fun EmotionsBottomSheetPreview() {
    EmotionBottomSheetLayout(
        emotions = listOf(Sad, Angry, Calm, Happy),
        onClick = {}
    )
}