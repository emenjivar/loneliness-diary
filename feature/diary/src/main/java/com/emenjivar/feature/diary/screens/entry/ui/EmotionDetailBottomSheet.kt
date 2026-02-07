package com.emenjivar.feature.diary.screens.entry.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emenjivar.core.data.models.EmotionData
import com.emenjivar.core.data.models.Mocks
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Stable
fun EmotionViewBottomSheet(
    sheetState: BottomSheetStateWithData<EmotionData>
) {
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet = sheetState.showBottomSheet.collectAsStateWithLifecycle()

    if (showBottomSheet.value) {
        ModalBottomSheet(
            sheetState = sheetState.sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                }
            }
        ) {
            EmotionViewBottomSheetLayout(
                emotion = sheetState.data
            )
        }
    }
}

@Composable
@Stable
private fun EmotionViewBottomSheetLayout(
    emotion: EmotionData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = emotion.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = emotion.description
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = "Function",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Sadness is believed to serve two primary functions that enhance one's ability to cope loss."
            )
            // TODO: might be more convenient to use a markdown format to load arbitraty information
        }
    }
}

@Preview
@Composable
private fun EmotionViewBottomSheetLayoutPreview() {
    EmotionViewBottomSheetLayout(
        emotion = Mocks.emotion1
    )
}
