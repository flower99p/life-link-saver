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
    selectedCategory: String = "Masuk"
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
                            text = "Tempel link untuk disimpan...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontSize = 18.sp
                        )
                    }
                    innerTextField()
                }

                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Simpan ke",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .height(38.dp)
                    .width(110.dp)
                    .background(Color(0xFFC9D8BA), RoundedCornerShape(18.dp))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF3F4738)
                    )
                    Text(selectedCategory, color = Color(0xFF3F4738), fontWeight = FontWeight.Medium)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF3F4738)
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.height(38.dp)
            ) {
                Text("Simpan", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
