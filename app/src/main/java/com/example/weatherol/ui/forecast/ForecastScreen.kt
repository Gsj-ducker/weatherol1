package com.example.weatherol.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherol.AppState
import com.example.weatherol.data.common.DataResult
import com.example.weatherol.data.remote.model.WeatherResponse
import com.example.weatherol.data.repository.WeatherRepository

// 统一数据层：真实天气接口
// 页面：你喜欢的漂亮UI

@Composable
fun ForecastScreen(lat: Double = 39.9042, lon: Double = 116.4074) {
    val repo = remember { WeatherRepository() }
    val state = remember { mutableStateOf<DataResult<WeatherResponse>?>(null) }
    val isCelsius = AppState.isCelsius.value

    LaunchedEffect(lat, lon) {
        state.value = repo.fetchWeather(lat, lon)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "未来预报",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(20.dp))

        when (val current = state.value) {
            is DataResult.Success -> {
                val data = current.data
                val hourly = data.hourly
                val daily = data.daily

                // ========== 24小时预报 ==========
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("24小时预报", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(12.dp))

                        val hList = mutableListOf<HourlyItem>()
                        repeat(8) { i ->
                            val t = hourly?.time?.getOrNull(i)?.takeLast(5) ?: ""
                            val temp = hourly?.temperature2m?.getOrNull(i) ?: 0.0
                            hList.add(HourlyItem(t, temp))
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(hList) {
                                val tempText = if (isCelsius) {
                                    "%.1f°C".format(it.temp)
                                } else {
                                    "%.1f°F".format(it.temp * 9 / 5 + 32)
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(it.time, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(Modifier.height(8.dp))
                                    Image(
                                        painter = painterResource(android.R.drawable.ic_menu_compass),
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(tempText, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(30.dp))

                // ========== 7天预报 ==========
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("7天预报", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(12.dp))

                        val dayNames = listOf("今天", "明天", "周三", "周四", "周五", "周六", "周日")
                        dayNames.forEachIndexed { index, day ->
                            val max = daily?.temperature2mMax?.getOrNull(index) ?: 0.0
                            val min = daily?.temperature2mMin?.getOrNull(index) ?: 0.0

                            val maxStr = if (isCelsius) {
                                "%.1f°C".format(max)
                            } else {
                                "%.1f°F".format(max * 9 / 5 + 32)
                            }
                            val minStr = if (isCelsius) {
                                "%.1f°C".format(min)
                            } else {
                                "%.1f°F".format(min * 9 / 5 + 32)
                            }

                            DailyForecastItem(day, "晴", maxStr, minStr)

                            if (index != dayNames.lastIndex) {
                                Divider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
            }

            is DataResult.Error -> {
                Text("错误: ${current.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
            }

            else -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

private data class HourlyItem(val time: String, val temp: Double)

@Composable
private fun DailyForecastItem(day: String, desc: String, high: String, low: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(day, fontWeight = FontWeight.Medium)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(android.R.drawable.ic_menu_compass),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(desc, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("$high / $low")
    }
}