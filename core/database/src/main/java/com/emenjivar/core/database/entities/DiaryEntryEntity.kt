package com.emenjivar.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "diary_entry")
data class DiaryEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "text")
    val content: String
)

data class DiaryEntryWithInsertionsEntity(
    @Embedded
    val entry: DiaryEntryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "entry_id"
    )
    val emotions: List<DiaryEntryEmotionEntity>
)
