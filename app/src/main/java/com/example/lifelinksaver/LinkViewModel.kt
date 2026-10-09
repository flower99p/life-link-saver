package com.example.lifelinksaver

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifelinksaver.data.db.AppDatabase
import com.example.lifelinksaver.data.db.SavedLinkEntity
import com.example.lifelinksaver.data.remote.LinkMetadataFetcher
import com.example.lifelinksaver.data.repository.LinkRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LinkViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = LinkRepository(AppDatabase.getDatabase(app).savedLinkDao())

    val links: StateFlow<List<SavedLinkEntity>> = repository.getAllLinks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Saves the link immediately, then fills in title and thumbnail once fetched. */
    fun addLink(url: String, title: String, notes: String, category: String) {
        viewModelScope.launch {
            val entity = SavedLinkEntity(url = url, title = title, notes = notes, category = category)
            val id = repository.insertLink(entity).toInt()
            val meta = LinkMetadataFetcher.fetch(url)
            repository.updateLink(
                entity.copy(
                    id = id,
                    title = title.ifBlank { meta.title ?: hostOf(url) },
                    thumbnailUrl = meta.thumbnailUrl
                )
            )
        }
    }

    fun deleteLink(link: SavedLinkEntity) {
        viewModelScope.launch { repository.deleteLink(link) }
    }

    fun updateLink(link: SavedLinkEntity, url: String, title: String, notes: String, category: String) {
        viewModelScope.launch {
            val updated = link.copy(url = url, title = title, notes = notes, category = category)
            repository.updateLink(updated)
            if (url != link.url) {
                val meta = LinkMetadataFetcher.fetch(url)
                repository.updateLink(
                    updated.copy(
                        title = title.ifBlank { meta.title ?: hostOf(url) },
                        thumbnailUrl = meta.thumbnailUrl
                    )
                )
            }
        }
    }

    fun refreshThumbnail(link: SavedLinkEntity) {
        viewModelScope.launch {
            val meta = LinkMetadataFetcher.fetch(link.url)
            repository.updateLink(
                link.copy(
                    thumbnailUrl = meta.thumbnailUrl ?: link.thumbnailUrl,
                    title = if (link.title.isBlank() ||
                        LinkMetadataFetcher.isTikTokPlaceholderTitle(link.url, link.title)
                    ) {
                        meta.title ?: hostOf(link.url)
                    } else {
                        link.title
                    }
                )
            )
        }
    }

    companion object {
        fun hostOf(url: String): String =
            try {
                java.net.URI(url).host?.removePrefix("www.") ?: url
            } catch (e: Exception) {
                url
            }
    }
}
