package com.example.lifelinksaver.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedDoodleScene() {
    val infiniteTransition = rememberInfiniteTransition(label = "doodle")
    val bobY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobY"
    )

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatOffset"
    )

    val blink by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
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
                    .background(Color(0xFF121212), RoundedCornerShape(40.dp))
            )

            Box(
                modifier = Modifier
                    .offset(y = (-18).dp)
                    .size(width = 174.dp, height = 84.dp)
                    .background(Color(0xFFB7B9A0), RoundedCornerShape(80.dp))
                    .border(2.dp, Color(0xFF6F6F67), RoundedCornerShape(80.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AnimatedEye(blink)
                    AnimatedEye(blink)
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-10).dp, y = (-20).dp)
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
                    translationY = floatOffset * 0.8f
                    rotationZ = -6f
                }
        ) {
            DoodleLeaf()
        }
    }
}

@Composable
fun AnimatedEye(blink: Float) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .background(Color(0xFF1B1A1A), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size((8 * blink).dp)
                .background(Color(0xFFF8F8F7), CircleShape)
        )
    }
}

@Composable
fun FloatingCard(
    modifier: Modifier = Modifier,
    text: String,
    subtitle: String
) {
    Box(
        modifier = modifier
            .size(width = 92.dp, height = 118.dp)
            .background(Color(0xFFF4F4F2), RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFE2E2E0), RoundedCornerShape(14.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            androidx.compose.material3.Text(
                text = "TEMUANMU",
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                color = Color(0xFF4C4C4C)
            )
            Spacer(Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = text,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                color = Color(0xFF4C4C4C)
            )
            Spacer(Modifier.height(2.dp))
            androidx.compose.material3.Text(
                text = subtitle,
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                color = Color(0xFF7A7A7A)
            )
        }
    }
}

@Composable
fun FloatingPlant(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(70.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(44.dp, 18.dp)
                .background(Color(0xFFB8A68A), RoundedCornerShape(9.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(18.dp, 36.dp)
                .background(Color(0xFFB4C7A3), RoundedCornerShape(20.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(18.dp, 22.dp)
                .background(Color(0xFFB3C89B), RoundedCornerShape(18.dp))
        )
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