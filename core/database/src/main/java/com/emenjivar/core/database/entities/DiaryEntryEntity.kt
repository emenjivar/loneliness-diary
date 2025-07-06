package com.emenjivar.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entry")
class DiaryEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "text")
    val content: String
)