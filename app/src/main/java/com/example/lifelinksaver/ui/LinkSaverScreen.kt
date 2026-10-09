package com.example.lifelinksaver.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.lifelinksaver.LinkViewModel
import com.example.lifelinksaver.data.db.SavedLinkEntity
import com.example.lifelinksaver.data.remote.LinkMetadataFetcher

private val Categories = listOf("Ide", "Inspirasi", "Belajar", "Lainnya")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkSaverScreen(
    viewModel: LinkViewModel,
    sharedText: String?,
    onSharedTextConsumed: () -> Unit,
    onOpenLink: (String) -> Unit
) {
    val links by viewModel.links.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { context.getSharedPreferences("settings", android.content.Context.MODE_PRIVATE) }
    var linkLayout by rememberSaveable { mutableStateOf(prefs.getString("link_layout", "Line") ?: "Line") }
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var showAdd by rememberSaveable { mutableStateOf(false) }
    var showLayoutMenu by rememberSaveable { mutableStateOf(false) }
    var prefill by rememberSaveable { mutableStateOf("") }
    var editingLink by remember { mutableStateOf<SavedLinkEntity?>(null) }
    val showSearch = selectedTab == 1
    val categoryChipColors = FilterChipDefaults.filterChipColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
    )

    LaunchedEffect(sharedText) {
        if (sharedText != null) {
            prefill = Regex("https?://\\S+").find(sharedText)?.value ?: sharedText
            showAdd = true
            onSharedTextConsumed()
        }
    }

    val visible = links.filter {
        (filter == null || it.category == filter) &&
            (query.isBlank() ||
            LinkMetadataFetcher.normalizeTitle(it.title).contains(query, true) ||
                it.url.contains(query, true) ||
                it.notes.contains(query, true))
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(if (showSearch) "Cari" else "Beranda") },
                actions = {
                    if (!showSearch) {
                        Box {
                            IconButton(onClick = { showLayoutMenu = true }) {
                                Icon(
                                    imageVector = when (linkLayout) {
                                        "Compact" -> Icons.Default.ViewAgenda
                                        "Grid 3" -> Icons.Default.GridView
                                        else -> Icons.Default.ViewList
                                    },
                                    contentDescription = "Pilih tata letak"
                                )
                            }
                            DropdownMenu(
                                expanded = showLayoutMenu,
                                onDismissRequest = { showLayoutMenu = false }
                            ) {
                                listOf("Line", "Compact", "Grid 3").forEach { layout ->
                                    DropdownMenuItem(
                                        text = { Text(if (linkLayout == layout) "✓ $layout" else layout) },
                                        onClick = {
                                            linkLayout = layout
                                            prefs.edit().putString("link_layout", layout).apply()
                                            showLayoutMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFF3EDF5)) {
                NavigationBarItem(
                    selected = !showSearch,
                    onClick = { selectedTab = 0; query = "" },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Beranda") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF51466B),
                        selectedTextColor = Color(0xFF51466B),
                        indicatorColor = Color(0xFFE8DDF4),
                        unselectedIconColor = Color(0xFF514D56),
                        unselectedTextColor = Color(0xFF514D56)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Cari") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF51466B),
                        selectedTextColor = Color(0xFF51466B),
                        indicatorColor = Color(0xFFE8DDF4),
                        unselectedIconColor = Color(0xFF514D56),
                        unselectedTextColor = Color(0xFF514D56)
                    )
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { prefill = ""; showAdd = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Simpan link") }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            if (showSearch) OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Cari link") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Hapus pencarian")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = filter == null,
                        onClick = { filter = null },
                        label = { Text("Semua (${links.size})") },
                        colors = categoryChipColors,
                        border = null
                    )
                }
                items(Categories) { cat ->
                    FilterChip(
                        selected = filter == cat,
                        onClick = { filter = if (filter == cat) null else cat },
                        label = { Text("$cat (${links.count { it.category == cat }})") },
                        colors = categoryChipColors,
                        border = null
                    )
                }
            }

            if (visible.isEmpty()) {
                EmptyState(hasLinks = links.isNotEmpty())
            } else if (linkLayout == "Grid 3") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    gridItems(visible, key = { it.id }) { link ->
                        LinkCard(
                            link = link,
                            layout = linkLayout,
                            onOpen = { onOpenLink(link.url) },
                            onDelete = { viewModel.deleteLink(link) },
                            onEdit = { editingLink = link },
                            onRefresh = { viewModel.refreshThumbnail(link) }
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visible, key = { it.id }) { link ->
                        LinkCard(
                            link = link,
                            layout = linkLayout,
                            onOpen = { onOpenLink(link.url) },
                            onDelete = { viewModel.deleteLink(link) },
                            onEdit = { editingLink = link },
                            onRefresh = { viewModel.refreshThumbnail(link) }
                        )
                    }
                }
            }
        }
    }

    if (showAdd) {
        AddLinkDialog(
            initialUrl = prefill,
            onDismiss = { showAdd = false },
            onSave = { url, title, notes, category ->
                viewModel.addLink(url, title, notes, category)
                showAdd = false
            }
        )
    }

    editingLink?.let { link ->
        EditLinkDialog(
            link = link,
            onDismiss = { editingLink = null },
            onSave = { url, title, notes, category ->
                viewModel.updateLink(link, url, title, notes, category)
                editingLink = null
            }
        )
    }
}

@Composable
private fun EmptyState(hasLinks: Boolean) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Link,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                if (hasLinks) "Tidak ada link yang cocok" else "Belum ada link tersimpan",
                style = MaterialTheme.typography.titleMedium
            )
            if (!hasLinks) {
                Text(
                    "Ketuk \"Simpan link\" atau bagikan link dari aplikasi lain.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LinkCard(
    link: SavedLinkEntity,
    layout: String,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onRefresh: () -> Unit
) {
    ElevatedCard(
        onClick = onOpen,
        modifier = Modifier.fillMaxWidth()
    ) {
        when (layout) {
            "Compact" -> Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinkThumbnail(link, Modifier.size(width = 96.dp, height = 80.dp))
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(
                        LinkMetadataFetcher.normalizeTitle(link.title).ifBlank { LinkViewModel.hostOf(link.url) },
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        LinkViewModel.hostOf(link.url),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            link.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        if (link.thumbnailUrl == null) {
                            TextButton(onClick = onRefresh) { Text("Muat thumbnail") }
                        }
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus")
                        }
                    }
                }
            }
            "Grid 3" -> {
                LinkThumbnail(link, Modifier.fillMaxWidth().aspectRatio(1f))
                Column(Modifier.padding(start = 8.dp, top = 6.dp, end = 4.dp, bottom = 2.dp)) {
                    Text(
                        LinkMetadataFetcher.normalizeTitle(link.title).ifBlank { LinkViewModel.hostOf(link.url) },
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (link.thumbnailUrl == null) {
                            IconButton(onClick = onRefresh, modifier = Modifier.size(40.dp)) {
                                Icon(Icons.Default.Link, contentDescription = "Muat thumbnail")
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = onEdit, modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
            else -> {
                LinkThumbnail(link, Modifier.fillMaxWidth().aspectRatio(16f / 9f))
                Column(Modifier.padding(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 8.dp)) {
                    Text(
                        LinkMetadataFetcher.normalizeTitle(link.title).ifBlank { LinkViewModel.hostOf(link.url) },
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        LinkViewModel.hostOf(link.url),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (link.notes.isNotBlank()) {
                        Text(
                            link.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            link.category,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        if (link.thumbnailUrl == null) {
                            TextButton(onClick = onRefresh) { Text("Muat thumbnail") }
                        }
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = onOpen) {
                            Icon(Icons.Default.OpenInBrowser, contentDescription = "Buka")
                        }
                        IconButton(onClick = onDelete) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LinkThumbnail(link: SavedLinkEntity, modifier: Modifier) {
    Box(modifier) {
        if (link.thumbnailUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(link.thumbnailUrl)
                    .size(320, 320)
                    .allowHardware(true)
                    .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
                    .crossfade(false)
                    .build(),
                contentDescription = LinkMetadataFetcher.normalizeTitle(link.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Link,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun AddLinkDialog(
    initialUrl: String,
    onDismiss: () -> Unit,
    onSave: (url: String, title: String, notes: String, category: String) -> Unit
) {
    LinkEditorDialog(
        initialUrl = initialUrl,
        initialTitle = "",
        initialNotes = "",
        initialCategory = Categories.first(),
        dialogTitle = "Simpan link",
        onDismiss = onDismiss,
        onSave = onSave
    )
}

@Composable
private fun EditLinkDialog(
    link: SavedLinkEntity,
    onDismiss: () -> Unit,
    onSave: (url: String, title: String, notes: String, category: String) -> Unit
) {
    LinkEditorDialog(
        initialUrl = link.url,
        initialTitle = LinkMetadataFetcher.normalizeTitle(link.title),
        initialNotes = link.notes,
        initialCategory = link.category,
        dialogTitle = "Edit link",
        onDismiss = onDismiss,
        onSave = onSave
    )
}

@Composable
private fun LinkEditorDialog(
    initialUrl: String,
    initialTitle: String,
    initialNotes: String,
    initialCategory: String,
    dialogTitle: String,
    onDismiss: () -> Unit,
    onSave: (url: String, title: String, notes: String, category: String) -> Unit
) {
    var url by rememberSaveable { mutableStateOf(initialUrl) }
    var title by rememberSaveable { mutableStateOf(initialTitle) }
    var notes by rememberSaveable { mutableStateOf(initialNotes) }
    var category by rememberSaveable { mutableStateOf(initialCategory) }
    val normalized = LinkMetadataFetcher.normalizeUrl(url)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL") },
                    singleLine = true,
                    isError = url.isNotBlank() && normalized == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul (opsional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan (opsional)") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Categories) { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = normalized != null,
                onClick = { normalized?.let { onSave(it, title.trim(), notes.trim(), category) } }
            ) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
