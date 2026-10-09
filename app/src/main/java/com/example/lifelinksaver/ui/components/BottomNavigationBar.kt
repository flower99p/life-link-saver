package com.example.lifelinksaver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem("Home", selectedTab == "Home", Icons.Default.Home, onTabClick)
        NavigationItem("Saved", selectedTab == "Saved", Icons.Default.Bookmark, onTabClick)
        NavigationItem("Profile", selectedTab == "Profile", Icons.Default.Person, onTabClick)
        NavigationItem("Settings", selectedTab == "Settings", Icons.Default.Settings, onTabClick)
    }
}

@Composable
private fun NavigationItem(
    label: String,
    selected: Boolean,
    icon: ImageVector,
    onTabClick: (String) -> Unit
) {
    val tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    TextButton(onClick = { onTabClick(label) }) {
        Icon(imageVector = icon, contentDescription = label, tint = tint)
        Spacer(Modifier.width(6.dp))
        Text(text = label, color = tint)
    }
}
