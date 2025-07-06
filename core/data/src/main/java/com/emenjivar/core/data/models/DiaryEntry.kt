package com.emenjivar.core.data.models

import com.emenjivar.core.database.entities.DiaryEntryEntity

data class DiaryEntry(
    val uid: Int = 0,
    val title: String,
    val content: String
)

fun DiaryEntryEntity.toModel() = DiaryEntry(
    uid = uid,
    title = title,
    content = content
)

fun DiaryEntry.toEntity() = DiaryEntryEntity(
    uid = uid,
    title = title,
    content = content
)
