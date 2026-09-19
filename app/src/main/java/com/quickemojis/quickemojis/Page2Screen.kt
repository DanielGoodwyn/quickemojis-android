package com.quickemojis.quickemojis

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun Page2Screen() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    var bgColor by remember { mutableStateOf(randomColor()) }
    var circleColor by remember { mutableStateOf(randomColor()) }
    var emoji by remember { mutableStateOf<EmojiItem?>(null) }
    
    var targetX by remember { mutableStateOf(0f) }
    var targetY by remember { mutableStateOf(0f) }
    var targetSize by remember { mutableStateOf(100f) }
    
    val animatedX by animateFloatAsState(
        targetValue = targetX,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
    )
    val animatedY by animateFloatAsState(
        targetValue = targetY,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
    )
    val animatedSize by animateFloatAsState(
        targetValue = targetSize,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
    )
    
    val animatedBg by animateColorAsState(targetValue = bgColor)
    val animatedCircle by animateColorAsState(targetValue = circleColor)

    fun randomize() {
        bgColor = randomColor()
        circleColor = randomColor()
        val e = EmojiData.emojis.randomOrNull()
        emoji = e
        e?.let { EmojiData.speak(it.name) }
        
        val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
        val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
        
        val minSize = 200f
        val maxSize = minOf(screenWidth, screenHeight) * 0.8f
        targetSize = Random.nextFloat() * (maxSize - minSize) + minSize
        
        val maxX = maxOf(0f, screenWidth - targetSize)
        val maxY = maxOf(0f, screenHeight - targetSize)
        
        targetX = Random.nextFloat() * maxX
        targetY = Random.nextFloat() * maxY
    }

    LaunchedEffect(Unit) {
        if (emoji == null) {
            randomize()
        }
    }
    
    LaunchedEffect(configuration) {
        val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
        val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
        
        val maxX = maxOf(0f, screenWidth - targetSize)
        val maxY = maxOf(0f, screenHeight - targetSize)
        
        if (targetX > maxX) targetX = maxX
        if (targetY > maxY) targetY = maxY
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBg)
            .clickable { randomize() }
    ) {
        emoji?.let { item ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .offset { IntOffset(animatedX.toInt(), animatedY.toInt()) }
                    .size(with(density) { animatedSize.toDp() })
                    .background(animatedCircle, CircleShape)
            ) {
                Text(
                    text = item.emoji,
                    fontSize = with(density) { (animatedSize * 0.6f).toSp() }
                )
            }
        }
    }
}

private fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}
