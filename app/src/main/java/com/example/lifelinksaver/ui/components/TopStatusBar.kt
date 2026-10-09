package com.example.lifelinksaver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopStatusBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(240.dp)
                .height(28.dp)
                .background(Color(0xFF111111), shape = RoundedCornerShape(18.dp))
        )

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("10:59", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("00.05", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp, 10.dp)
                        .background(Color.Black, RoundedCornerShape(2.dp))
                )
                Box(
                    modifier = Modifier
                        .size(10.dp, 10.dp)
                        .background(Color.Black, CircleShape)
                )
            }
        }
    }
}
