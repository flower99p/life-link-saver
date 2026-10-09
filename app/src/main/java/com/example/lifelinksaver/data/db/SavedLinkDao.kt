package com.example.lifelinksaver.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedLinkDao {
    @Insert
    suspend fun insertLink(link: SavedLinkEntity): Long

    @Update
    suspend fun updateLink(link: SavedLinkEntity)

    @Delete
    suspend fun deleteLink(link: SavedLinkEntity)

    @Query("SELECT * FROM saved_links ORDER BY createdAt DESC")
    fun getAllLinks(): Flow<List<SavedLinkEntity>>

    @Query("SELECT * FROM saved_links WHERE category = :category ORDER BY createdAt DESC")
    fun getLinksByCategory(category: String): Flow<List<SavedLinkEntity>>

    @Query("SELECT COUNT(*) FROM saved_links WHERE category = :category")
    fun getLinkCountByCategory(category: String): Flow<Int>

    @Query("SELECT * FROM saved_links WHERE id = :id")
    suspend fun getLinkById(id: Int): SavedLinkEntity?

    @Query("DELETE FROM saved_links WHERE id = :id")
    suspend fun deleteLinkById(id: Int)
}