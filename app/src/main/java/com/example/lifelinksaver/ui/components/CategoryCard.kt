package com.example.lifelinksaver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabClick: (String) -> Unit
) {
    val tabs = listOf(
        TabItem("Home", Icons.Default.Home),
        TabItem("Saved", Icons.Default.Bookmark),
        TabItem("Profile", Icons.Default.AccountCircle),
        TabItem("Settings", Icons.Default.Settings)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val selected = tab.name == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(68.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (selected) Color(0xFFDDE9D5) else Color(0xFFE8E7E5)
                    )
                    .clickable { onTabClick(tab.name) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.name,
                    tint = if (selected) Color(0xFF2C2C2C) else Color(0xFF7E7E7E),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

data class TabItem(val name: String, val icon: ImageVector)
