package com.example.lifelinksaver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaveLinkInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    selectedCategory: String = \"Masuk\"
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFFEAE7E4), RoundedCornerShape(34.dp))
                .border(1.dp, Color(0xFFE2E2E0), RoundedCornerShape(34.dp))
                .padding(horizontal = 18.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(Modifier.width(10.dp))

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 10.dp),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                ) { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = \"Tempel link untuk disimpan...\",\n                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),\n                            fontSize = 18.sp\n                        )
                    }
                    innerTextField()
                }

                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier\n                        .size(20.dp)\n                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)\n                )
            }
        }

        Spacer(Modifier.height(18.dp))

        Row(\n            modifier = Modifier.fillMaxWidth(),\n            horizontalArrangement = Arrangement.Center,\n            verticalAlignment = Alignment.CenterVertically\n        ) {\n            Text(\n                text = \"Simpan ke\",\n                style = MaterialTheme.typography.bodyLarge,\n                color = MaterialTheme.colorScheme.onSurfaceVariant\n            )

            Spacer(Modifier.width(10.dp))

            Box(\n                modifier = Modifier\n                    .height(38.dp)\n                    .width(110.dp)\n                    .background(Color(0xFFC9D8BA), RoundedCornerShape(18.dp))\n                    .padding(horizontal = 14.dp),\n                contentAlignment = Alignment.Center\n            ) {\n                Row(\n                    verticalAlignment = Alignment.CenterVertically,\n                    horizontalArrangement = Arrangement.SpaceBetween,\n                    modifier = Modifier.fillMaxWidth()\n                ) {\n                    Icon(\n                        imageVector = Icons.Default.FolderOpen,\n                        contentDescription = null,\n                        modifier = Modifier.size(18.dp),\n                        tint = Color(0xFF3F4738)\n                    )\n                    Text(selectedCategory, color = Color(0xFF3F4738), fontWeight = FontWeight.Medium)\n                    Icon(\n                        imageVector = Icons.Default.ArrowDropDown,\n                        contentDescription = null,\n                        modifier = Modifier.size(18.dp),\n                        tint = Color(0xFF3F4738)\n                    )\n                }\n            }

            Spacer(Modifier.width(10.dp))

            Button(\n                onClick = onSave,\n                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),\n                shape = RoundedCornerShape(18.dp),\n                modifier = Modifier.height(38.dp)\n            ) {\n                Text(\"Simpan\", color = MaterialTheme.colorScheme.onSurfaceVariant)\n            }\n        }\n    }\n}\nEOF

cat > "$ROOT_DIR/app/src/main/java/com/example/lifelinksaver/ui/components/BottomNavigationBar.kt" <<'EOF'
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
        TabItem(\"Home\", Icons.Default.Home),
        TabItem(\"Saved\", Icons.Default.Bookmark),
        TabItem(\"Profile\", Icons.Default.AccountCircle),
        TabItem(\"Settings\", Icons.Default.Settings)
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
