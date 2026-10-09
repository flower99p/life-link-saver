package com.example.lifelinksaver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.lifelinksaver.ui.components.AnimatedDoodleScene
import com.example.lifelinksaver.ui.components.BottomNavigationBar
import com.example.lifelinksaver.ui.components.CategoryCard
import com.example.lifelinksaver.ui.components.SaveLinkInput
import com.example.lifelinksaver.ui.components.TopStatusBar
import com.example.lifelinksaver.ui.theme.LifeLinkSaverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeLinkSaverTheme {
                LifeLinkSaverApp()
            }
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    @Composable
    fun LifeLinkSaverApp() {
        var urlInput by remember { mutableStateOf("") }
        var selectedTab by remember { mutableStateOf("Home") }
        var selectedCategory by remember { mutableStateOf("Masuk") }

        val savedLinks = remember {
            mutableStateListOf(
                LinkItem(1, "https://example.com", "Example", "Masuk"),
                LinkItem(2, "https://instagram.com", "Instagram", "Masuk"),
                LinkItem(3, "https://github.com", "GitHub", "Masuk")
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                "Home" -> HomeScreen(
                    urlInput = urlInput,
                    onUrlInputChange = { urlInput = it },
                    savedLinks = savedLinks,
                    selectedCategory = selectedCategory,
                    onSaveLink = {
                        if (urlInput.isNotBlank()) {
                            savedLinks.add(
                                LinkItem(
                                    id = savedLinks.size + 1,
                                    url = urlInput,
                                    title = extractDomain(urlInput),
                                    category = selectedCategory
                                )
                            )
                            urlInput = ""
                        }
                    }
                )

                "Saved" -> SavedLinksScreen(
                    links = savedLinks,
                    onDeleteLink = { link -> savedLinks.remove(link) },
                    onOpenLink = { url -> openLink(url) }
                )

                "Profile" -> ProfileScreen()
                "Settings" -> SettingsScreen()
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabClick = { selectedTab = it }
                )
            }
        }
    }

    @Composable
    fun HomeScreen(
        urlInput: String,
        onUrlInputChange: (String) -> Unit,
        savedLinks: List<LinkItem>,
        selectedCategory: String,
        onSaveLink: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            TopStatusBar()

            Spacer(Modifier.height(18.dp))

            Text(
                text = "LIFE",
                style = MaterialTheme.typography.headlineLarge.copy(
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Light
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KOLEKSI",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Semua >",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CategoryCard(
                    title = "Ide",
                    accent = Color(0xFFE9E8E5),
                    count = savedLinks.count { it.category == "Ide" },
                    selected = true
                )
                CategoryCard(
                    title = "Inspirasi",
                    accent = Color(0xFFDDE4D5),
                    count = savedLinks.count { it.category == "Inspirasi" },
                    selected = false
                )
            }

            Spacer(Modifier.height(28.dp))
            AnimatedDoodleScene()

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Senang ditemani",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Elus, ajak bermain, atau beri temuan.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(26.dp))

            SaveLinkInput(
                value = urlInput,
                onValueChange = onUrlInputChange,
                onSave = onSaveLink,
                selectedCategory = selectedCategory
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Tersimpan di $selectedCategory. Kamu bisa menambahkan judul dan catatan.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(100.dp))
        }
    }

    @Composable
    fun SavedLinksScreen(
        links: List<LinkItem>,
        onDeleteLink: (LinkItem) -> Unit,
        onOpenLink: (String) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            TopStatusBar()
            Spacer(Modifier.height(20.dp))

            Text(
                text = "Temuan Saya",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(18.dp))

            if (links.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada link yang disimpan",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(links) { link ->
                        LinkCard(
                            link = link,
                            onDelete = { onDeleteLink(link) },
                            onOpen = { onOpenLink(link.url) }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun LinkCard(
        link: LinkItem,
        onDelete: () -> Unit,
        onOpen: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpen() }
                ) {
                    Text(
                        text = link.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = link.url,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = link.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onOpen, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "Buka",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ProfileScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Profil",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
    
    @Composable
    fun SettingsScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pengaturan",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
    
    data class LinkItem(
        val id: Int,
        val url: String,
        val title: String,
        val category: String
    )

    private fun extractDomain(url: String): String {
        return try {
            val uri = Uri.parse(url)
            uri.host?.removePrefix("www.") ?: url
        } catch (e: Exception) {
            url
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewApp() {
    LifeLinkSaverTheme {
        val activity = MainActivity()
        // preview tidak menjalankan startActivity, hanya rendering UI
    }
}
