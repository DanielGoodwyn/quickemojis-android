package com.quickemojis.quickemojis

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun Page1Screen() {
    val context = LocalContext.current
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val emojis = EmojiData.emojis
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    
    var toastEmoji by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }

    var lastScrubIndex by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            val scrubberModifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, _ ->
                        val y = change.position.y
                        val height = size.height
                        val percentage = (y / height).coerceIn(0f, 1f)
                        val index = (percentage * (emojis.size - 1)).toInt()
                        if (index != lastScrubIndex) {
                            lastScrubIndex = index
                            coroutineScope.launch {
                                gridState.scrollToItem(index)
                            }
                        }
                    }
                }
            
            Box(modifier = scrubberModifier)
            
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 360.dp)
                    .padding(vertical = 80.dp, horizontal = 24.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(10.dp)
                    .padding(top = 10.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    state = gridState,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(emojis.size) { index ->
                        val item = emojis[index]
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable {
                                    clipboard.setPrimaryClip(ClipData.newPlainText("emoji", item.emoji))
                                    EmojiData.speak(item.name)
                                    toastEmoji = item.emoji
                                    showToast = true
                                }
                        ) {
                            Text(text = item.emoji, fontSize = 36.sp)
                        }
                    }
                }
            }
            
            Box(modifier = scrubberModifier)
        }
        
        AnimatedVisibility(
            visible = showToast,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xAAFFFFFF), RoundedCornerShape(32.dp))
                    .padding(40.dp)
            ) {
                Text(text = toastEmoji, fontSize = 100.sp)
            }
        }
        
        if (showToast) {
            LaunchedEffect(toastEmoji) {
                kotlinx.coroutines.delay(1500)
                showToast = false
            }
        }
    }
}
