package com.emenjivar.feature.diary.screens.entry

enum class DiaryEntryAction(
    val title: String,
    val contentDescription: String
) {
    EMOTION(
        title = "Emotions",
        contentDescription = "Add emotion to diary entry"
    ),
    MUSIC(
        title = "Music",
        contentDescription = "Add a song to your diary entry"
    )
}
