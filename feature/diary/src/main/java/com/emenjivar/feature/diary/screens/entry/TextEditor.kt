package com.emenjivar.feature.diary.screens.entry

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

data class EmotionData(
    val name: String,
    val color: Color
)

val Sad = EmotionData(
    name = "sad",
    color = Color.Blue
)

val Angry = EmotionData(
    name = "angry",
    color = Color.Red
)


sealed class InsertedItem(
    val text: String,
    open val color: Color,
    open val startIndex: Int
) {
    class Emotion(
        data: EmotionData,
        override val startIndex: Int
    ) : InsertedItem(
        text = data.name,
        color = data.color,
        startIndex = startIndex
    )

    val length = text.length
}

fun buildInsertAnnotatedString(
    annotatedString: AnnotatedString,
    item: InsertedItem
): AnnotatedString {
    val insertedAnnotatedString = buildAnnotatedString {
        append(
            AnnotatedString(
                text = item.text,
                spanStyle = SpanStyle(color = item.color)
            )
        )
    }

    return annotatedString + insertedAnnotatedString
}

fun applyStylesToAnnotatedString(
    rawText: String,
    insertions: List<InsertedItem>
) = buildAnnotatedString {
    append(rawText)

    // Apply styles for valid insertions
    insertions.forEach { insertion ->
        if (insertion.startIndex < rawText.length) {
            val endIndex = minOf(
                insertion.startIndex + insertion.text.length,
                rawText.length
            )
            addStyle(
                style = SpanStyle(color = insertion.color),
                start = insertion.startIndex,
                end = endIndex
            )
        }
    }
}

