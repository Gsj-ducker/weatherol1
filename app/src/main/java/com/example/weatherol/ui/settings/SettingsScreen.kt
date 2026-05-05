package com.example.weatherol.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherol.AppState
@Composable
fun SettingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "设置",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        // 温度单位
        Text(text = "温度单位", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { AppState.isCelsius.value = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (AppState.isCelsius.value) AppState.themeColor.value else Color.Gray
                )
            ) {
                Text("摄氏度 ℃")
            }

            Button(
                onClick = { AppState.isCelsius.value = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!AppState.isCelsius.value) AppState.themeColor.value else Color.Gray
                )
            ) {
                Text("华氏度 ℉")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 主题颜色
        Text(text = "主题颜色", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorItem(Color(0xFF2196F3))    // 蓝
            ColorItem(Color(0xFF4CAF50))    // 绿
            ColorItem(Color(0xFF9C27B0))    // 紫
            ColorItem(Color(0xFFF44336))    // 红
        }
    }
}

@Composable
fun ColorItem(color: Color) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color, CircleShape)
            .clickable {
                AppState.themeColor.value = color
            }
    )
}