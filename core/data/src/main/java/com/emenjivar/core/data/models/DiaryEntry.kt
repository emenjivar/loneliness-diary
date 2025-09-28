package com.emenjivar.core.data.models

import com.emenjivar.core.database.entities.DiaryEntryEntity
import com.emenjivar.core.database.entities.DiaryEntryWithInsertionsEntity
import java.time.LocalDateTime

data class DiaryEntry(
    val id: Long = 0,
    val title: String,
    val content: String,
    val emotions: List<DiaryEntryEmotion>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null
)

fun DiaryEntryWithInsertionsEntity.toModel() = DiaryEntry(
    id = entry.id,
    title = entry.title,
    content = entry.content,
    emotions = emotions.map { it.toModel() },
    createdAt = entry.createdAt,
    updatedAt = entry.updatedAt
)

fun DiaryEntryEntity.toModel() = DiaryEntry(
    id = id,
    title = title,
    content = content,
    emotions = emptyList(),
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DiaryEntry.toEntity() = DiaryEntryWithInsertionsEntity(
    entry = DiaryEntryEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    ),
    emotions = emotions.map { it.toEntity() }
)
