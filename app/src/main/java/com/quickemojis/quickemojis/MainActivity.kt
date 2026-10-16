package com.quickemojis.quickemojis

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        EmojiData.init(this)
        
        setContent {
            val isDark = isSystemInDarkTheme()
            val outerColor = if (isDark) Color.White else Color.Black
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    val insetsController = WindowCompat.getInsetsController(window, view)
                    // In dark mode the outer box is white, so use dark system bar icons; in light mode outer box is black, so use light system bar icons
                    insetsController.isAppearanceLightStatusBars = isDark
                    insetsController.isAppearanceLightNavigationBars = isDark
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = outerColor
            ) {
                val pagerState = rememberPagerState(pageCount = { 2 })
                
                HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> Page1Screen()
                        1 -> Page2Screen()
                    }
                }
            }
        }
    }
}
