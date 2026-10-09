package com.example.lifelinksaver.data.repository

import com.example.lifelinksaver.data.db.SavedLinkDao
import com.example.lifelinksaver.data.db.SavedLinkEntity
import kotlinx.coroutines.flow.Flow

class LinkRepository(private val savedLinkDao: SavedLinkDao) {
    fun getAllLinks(): Flow<List<SavedLinkEntity>> = savedLinkDao.getAllLinks()

    fun getLinksByCategory(category: String): Flow<List<SavedLinkEntity>> =
        savedLinkDao.getLinksByCategory(category)

    fun getLinkCountByCategory(category: String): Flow<Int> =
        savedLinkDao.getLinkCountByCategory(category)

    suspend fun insertLink(link: SavedLinkEntity): Long = savedLinkDao.insertLink(link)

    suspend fun updateLink(link: SavedLinkEntity) = savedLinkDao.updateLink(link)

    suspend fun deleteLink(link: SavedLinkEntity) = savedLinkDao.deleteLink(link)

    suspend fun deleteLinkById(id: Int) = savedLinkDao.deleteLinkById(id)

    suspend fun getLinkById(id: Int): SavedLinkEntity? = savedLinkDao.getLinkById(id)
}