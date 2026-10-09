package com.example.lifelinksaver.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedDoodleScene() {
    val infiniteTransition = rememberInfiniteTransition(label = "doodle")

    val bobY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobY"
    )

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatOffset"
    )
    
    val blink by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.72f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        FloatingPlant(modifier = Modifier.align(Alignment.TopEnd).offset(x = (-18).dp, y = 12.dp))

        FloatingCard(
            modifier = Modifier.align(Alignment.CenterStart).offset(x = 26.dp, y = 30.dp),
            text = "4",
            subtitle = "hari ini"
        )
        
        Box(
            modifier = Modifier
                .offset(y = bobY.dp)
                .size(width = 210.dp, height = 120.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 230.dp, height = 72.dp)
                    .background(Color(0xFF181818), RoundedCornerShape(40.dp))
            )

            Box(
                modifier = Modifier
                    .offset(y = (-18).dp)
                    .size(width = 176.dp, height = 86.dp)
                    .background(Color(0xFFB6B9A3), RoundedCornerShape(80.dp))
                    .border(2.dp, Color(0xFF7A7B73), RoundedCornerShape(80.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedEye(blink)
                    AnimatedEye(blink)
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-6).dp, y = (-20).dp)
                .graphicsLayer {
                    translationY = floatOffset
                    rotationZ = 8f
                }
        ) {
            DoodleLeaf()
        }
        
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 18.dp, y = 10.dp)
                .graphicsLayer {
                    translationY = floatOffset * 0.85f
                    rotationZ = -6f
                }
        ) {
            DoodleLeaf()
        }
    }
}

@Composable
fun DoodleLeaf() {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(Color(0xFFD5DDD0), CircleShape)
    )
}
