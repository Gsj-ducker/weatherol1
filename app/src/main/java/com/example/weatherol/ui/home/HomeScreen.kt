package com.example.weatherol.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherol.data.common.DataResult
import com.example.weatherol.data.remote.model.WeatherResponse
import com.example.weatherol.data.repository.WeatherRepository

// 北京经纬度（你之后可以从城市页传过来）
private const val BEIJING_LAT = 39.9042
private const val BEIJING_LON = 116.4074

@Composable
fun HomeScreen() {
    // 1. 创建仓库实例
    val weatherRepository = WeatherRepository()

    // 2. 定义状态存储天气数据
    var weatherState by remember { mutableStateOf<DataResult<WeatherResponse>?>(null) }

    // 3. 页面一进来就请求数据
    LaunchedEffect(Unit) {
        weatherState = weatherRepository.fetchWeather(BEIJING_LAT, BEIJING_LON)
    }

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
                imageVector = Icons.Filled.LocationOn,
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

        // ======================
        // 这里开始显示真实温度！
        // ======================
        when (val state = weatherState) {
            is DataResult.Success -> {
                val weather = state.data
                val current = weather.current

                Text(
                    text = "${current?.temperature2m}°",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2980B9)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = getWeatherText(current?.weatherCode),
                    fontSize = 24.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherInfoCard(title = "湿度", value = "${current?.relativeHumidity2m}%")
                    WeatherInfoCard(title = "气压", value = "1012 hPa")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherInfoCard(title = "风速", value = "3.2 m/s")
                    WeatherInfoCard(title = "能见度", value = "10 km")
                }

            }
            is DataResult.Error -> {
                Text(text = "加载失败: ${state.message}", color = Color.Red)
            }
            else -> {
                CircularProgressIndicator() // 加载中
            }
        }
    }
}

// 根据天气代码返回文字
fun getWeatherText(code: Int?): String {
    return when (code) {
        0 -> "晴天"
        1,2,3 -> "多云"
        45,48 -> "雾"
        51,53,55 -> "小雨"
        61,63,65 -> "雨"
        71,73,75 -> "雪"
        else -> "未知"
    }
}

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