package com.emenjivar.core.data.models

import com.emenjivar.core.database.entities.DiaryEntryEmotionEntity

data class DiaryEntryEmotion(
    val id: Long = 0,
    val entryId: Long = 0L,
    val emotion: String,
    val insertionIndex: Int,
)

fun DiaryEntryEmotionEntity.toModel() = DiaryEntryEmotion(
    id = id,
    entryId = entryId,
    emotion = emotion,
    insertionIndex = insertionIndex
)

fun DiaryEntryEmotion.toEntity() = DiaryEntryEmotionEntity(
    id = id,
    entryId = entryId,
    emotion = emotion,
    insertionIndex = insertionIndex
)