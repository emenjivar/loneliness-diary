package com.emenjivar.feature.diary.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun InsertOptionMenu(
    modifier: Modifier = Modifier,
    onSelect: (InsertOption) -> Unit
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            InsertOption.entries.forEach { action ->
                Text(
                    text = action.label,
                    modifier = Modifier
                        .clickable(
                            onClick = { onSelect(action) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current
                        )
                        // TODO: add a sizes file for common dp measures
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

// TODO: Theme should be included in another module?
enum class InsertOption(val label: String) {
    EMOTION(label = "Emotion"),
    COLOR(label = "Color"),
    SONG(label = "Song"),
    PLACE(label = "Place")
}
