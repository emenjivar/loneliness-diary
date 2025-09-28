package com.emenjivar.core.data.models

/**
 * Represents an emotion with its visual and descriptive properties.
 *
 * @param name The display name of the emotion. Includes a trailing space
 *  for seamless text concatenation in UI context.
 * @param color The visual color representation of this emotion.
 * @param description A human-readable explanation of the emotion.
 */
data class EmotionData(
    val name: String,
    val color: Long,
    val description: String
) {
    companion object {
        val empty = EmotionData(
            name = "",
            // Default black
            color = 0xff000000,
            description = ""
        )
    }
}
