package com.example.lifelinksaver.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_links")
data class SavedLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val title: String = "",
    val notes: String = "",
    val category: String = "Ide",
    val createdAt: Long = System.currentTimeMillis(),
    val thumbnailUrl: String? = null
)
