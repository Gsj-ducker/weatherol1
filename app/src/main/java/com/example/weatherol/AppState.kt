package com.example.weatherol

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object AppState {
    // 用普通的 var + mutableStateOf，去掉 by 委托
    val isCelsius = mutableStateOf(true)
    val themeColor = mutableStateOf(Color(0xFF2196F3))
}