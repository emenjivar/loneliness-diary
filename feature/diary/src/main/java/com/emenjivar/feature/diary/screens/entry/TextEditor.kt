package com.emenjivar.feature.diary.screens.entry

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString

data class EmotionData(
    val name: String,
    val color: Color,
    val description: String
)

val Sad = EmotionData(
    name = "sad",
    color = Color(0xff0d47a1),
    description = "Feeling sorrow, typically in response to loss."
)

val Angry = EmotionData(
    name = "angry",
    color = Color(0xffb71c1c),
    description = "Feeling intense displeasure when facing perceived threats, injustice or blocked goals."
)

val Calm = EmotionData(
    name = "calm",
    color = Color(0xff1b5e20),
    description = "Feeling peaceful, relaxed and emotionally balanced."
)

val Happy = EmotionData(
    name = "happy",
    color = Color(0xfffdd835),
    description = "Feeling joy, contentment or pleasure from positive experiences or achievements."
)


sealed class InsertedItem(
    val text: String,
    open val color: Color,
    open val startIndex: Int
) {
    data class Emotion(
        val data: EmotionData,
        override val startIndex: Int
    ) : InsertedItem(
        text = data.name,
        color = data.color,
        startIndex = startIndex
    )

    val length = text.length
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

/**
 * Prevent an insertion when the cursor is within an existing insertion's range.
 *
 * @return true if insertions should be blocked, false otherwise
 */
fun shouldBlockInsertion(
    selection: TextRange,
    insertions: List<InsertedItem>
): Boolean {
    val cursorIndex = selection.start
    val cursorInsertedItem = insertions.firstOrNull {
        cursorIndex in (it.startIndex + 1)..<it.startIndex + it.length
    }
    return cursorInsertedItem != null
}

/**
 * Insert an element into the [insertions] list and adjust the subsequent insertion positions.
 * When inserting in the muddle of existing insertions, shift the start indices of all
 * following insertions by the length of the new element to maintain correct positioning.
 *
 * @param insertions The mutable list to insert into.
 * @param newElement The element to insert.
 */
fun insertItem(insertions: MutableList<InsertedItem>, newElement: InsertedItem) {
    insertions.apply {
        sortedBy { it.startIndex }

        if (isEmpty()) {
            add(newElement)
            return@apply // Early return
        }

        val before = filter { it.startIndex < newElement.startIndex }
        val after = filter { it.startIndex >= newElement.startIndex }
            .filterIsInstance<InsertedItem.Emotion>()
            .map { item -> item.copy(startIndex = item.startIndex + newElement.length) }

        val updatedList = before + listOf(newElement) + after
        clear()
        addAll(updatedList)
    }
}

/**
 * Inserts a text at a specific index position within the original string.
 *
 * @param original The original string to insert into.
 * @param newElement The element to insert.
 * @return The updated string with the new text inserted.
 */
fun insertText(original: String, newElement: InsertedItem): String {
    // Important to insert the new text in the original string
    val beforeText = original.substring(0, newElement.startIndex)
    val afterText = original.substring(newElement.startIndex)
    val updatedText = beforeText + newElement.text + afterText
    return updatedText
}
