package com.emenjivar.core.data.models

import com.emenjivar.core.database.entities.DiaryEntryEntity
import com.emenjivar.core.database.entities.DiaryEntryWithInsertionsEntity

data class DiaryEntry(
    val id: Long = 0,
    val title: String,
    val content: String,
    val emotions: List<DiaryEntryEmotion>
)

fun DiaryEntryWithInsertionsEntity.toModel() = DiaryEntry(
    id = entry.id,
    title = entry.title,
    content = entry.content,
    emotions = emotions.map { it.toModel() }
)

fun DiaryEntryEntity.toModel() = DiaryEntry(
    id = id,
    title = title,
    content = content,
    emotions = emptyList()
)

fun DiaryEntry.toEntity() = DiaryEntryWithInsertionsEntity(
    entry = DiaryEntryEntity(
        id = id,
        title = title,
        content = content
    ),
    emotions = emotions.map { it.toEntity() }
)
