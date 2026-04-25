package com.example.weatherol.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // 顶部城市名称
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "位置",
                tint = Color(0xFF6495ED)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "北京市",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 温度
        Text(
            text = "26°",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2980B9)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 天气状态
        Text(
            text = "多云",
            fontSize = 24.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 湿度 & 风速 卡片
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherInfoCard(title = "湿度", value = "45%")
            WeatherInfoCard(title = "风速", value = "3.2 m/s")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 气压 & 能见度 卡片
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherInfoCard(title = "气压", value = "1012 hPa")
            WeatherInfoCard(title = "能见度", value = "10 km")
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "今日天气适宜出行",
            fontSize = 16.sp,
            color = Color.DarkGray
        )
    }
}

// 小卡片组件（复用）
@Composable
fun WeatherInfoCard(title: String, value: String) {
    Card(
        modifier = Modifier.size(width = 150.dp, height = 90.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F7FA)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}