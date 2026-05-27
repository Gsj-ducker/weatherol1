package com.example.weatherol

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object AppState {
    // 温度
    val isCelsius = mutableStateOf(true)

    // 主题
    val themeColor = mutableStateOf(Color(0xFF2196F3))
    val isDarkTheme = mutableStateOf(false)

    // 🌍 全局城市经纬度（所有页面共用）
    val currentLat = mutableStateOf(39.9042)   // 默认北京
    val currentLon = mutableStateOf(116.4074)
    val currentCityName = mutableStateOf("北京")
}