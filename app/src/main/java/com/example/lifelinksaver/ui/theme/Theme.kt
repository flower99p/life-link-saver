package com.example.lifelinksaver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
}

@Composable
fun LifeLinkSaverApp() {
    var urlInput by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Home") }
    var selectedCategory by remember { mutableStateOf("Masuk") }

    val savedLinks = remember {
        mutableStateListOf(
            "https://example.com",
            "https://instagram.com",
            "https://github.com"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F0EC))
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
                CategoryCard("Ide", Color(0xFFE9E8E5), savedLinks.size, true)
                CategoryCard("Inspirasi", Color(0xFFDDE4D5), 0, false)
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
                onValueChange = { urlInput = it },
                onSave = {
                    if (urlInput.isNotBlank()) {
                        savedLinks.add(urlInput)
                        urlInput = ""
                    }
                },
                selectedCategory = selectedCategory
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Tersimpan di $selectedCategory. Kamu bisa menambahkan judul dan catatan.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(18.dp))
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

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewApp() {
    LifeLinkSaverTheme {
        LifeLinkSaverApp()
    }
}
