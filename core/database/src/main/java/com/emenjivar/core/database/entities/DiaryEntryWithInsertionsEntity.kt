package com.emenjivar.core.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class DiaryEntryWithInsertionsEntity(
    @Embedded
    val entry: DiaryEntryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "entry_id"
    )
    val emotions: List<DiaryEntryEmotionEntity>
)
