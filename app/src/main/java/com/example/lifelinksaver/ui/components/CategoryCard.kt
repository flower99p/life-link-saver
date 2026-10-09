package com.example.lifelinksaver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CategoryCard(
    title: String,
    accent: Color,
    count: Int,
    selected: Boolean
) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(accent)
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) Color(0xFFCECECE) else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4A4A4A)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF616161)
                )
            }
        }
    }
}