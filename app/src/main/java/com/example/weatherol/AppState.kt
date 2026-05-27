package com.example.weatherol

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object AppState {
    // 温度单位
    val isCelsius = mutableStateOf(true)

    // 主题颜色
    val themeColor = mutableStateOf(Color(0xFF2196F3))

    // 深色模式
    val isDarkTheme = mutableStateOf(false)
}