package com.example.weatherol.ui.forecast

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherol.AppState
import com.example.weatherol.data.common.DataResult
import com.example.weatherol.data.remote.model.WeatherResponse
import com.example.weatherol.data.repository.WeatherRepository

// 漂亮UI + 真实数据 完美融合版
@Composable
fun ForecastScreen(lat: Double = 39.9042, lon: Double = 116.4074) {
    val repo = remember { WeatherRepository() }
    val state = remember { androidx.compose.runtime.mutableStateOf<DataResult<WeatherResponse>?>(null) }
    val isCelsius = AppState.isCelsius.value

    LaunchedEffect(lat, lon) {
        state.value = repo.fetchWeather(lat, lon)
    }

    // 主界面：完全保留你喜欢的漂亮样式
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "未来预报",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (val current = state.value) {
            is DataResult.Success -> {
                val data = current.data
                val hourly = data.hourly
                val daily = data.daily

                // =============== 24小时预报（真实数据 + 漂亮UI） ===============
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "24小时预报",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        val hList = mutableListOf<HourlyItem>()
                        repeat(8) { i ->
                            val t = hourly?.time?.getOrNull(i)?.takeLast(5) ?: ""
                            val temp = hourly?.temperature2m?.getOrNull(i) ?: 0.0
                            hList.add(HourlyItem(t, temp))
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            items(hList) {
                                val tempText = if (isCelsius) {
                                    "%.1f°C".format(it.temp)
                                } else {
                                    "%.1f°F".format(it.temp * 9 / 5 + 32)
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(it.time, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(Modifier.height(8.dp))
                                    Image(
                                        painter = painterResource(android.R.drawable.ic_menu_compass),
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(tempText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // =============== 7天预报（真实数据 + 漂亮UI） ===============
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "7天预报",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(Modifier.height(12.dp))

                        val dayNames = listOf("今天", "明天", "周三", "周四", "周五", "周六", "周日")
                        dayNames.forEachIndexed { index, day ->
                            val max = daily?.temperature2mMax?.getOrNull(index) ?: 0.0
                            val min = daily?.temperature2mMin?.getOrNull(index) ?: 0.0

                            val maxStr = if (isCelsius) "%.1f°C" else "%.1f°F"
                            val minStr = if (isCelsius) "%.1f°C" else "%.1f°F"

                            val maxText = maxStr.format(if (isCelsius) max else max * 9/5 +32)
                            val minText = minStr.format(if (isCelsius) min else min * 9/5 +32)

                            DailyForecastItem(
                                day = day,
                                desc = "晴", // 如需天气描述，可从API补充
                                high = maxText,
                                low = minText
                            )

                            if (index != dayNames.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.outlineVariant)
                                )
                            }
                        }
                    }
                }
            }

            is DataResult.Error -> {
                Text(
                    "加载失败: ${current.message}",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppState.themeColor.value)
                }
            }
        }
    }
}

// 内部数据类
private data class HourlyItem(val time: String, val temp: Double)

// 你原来的漂亮列表项（完全保留）
@Composable
private fun DailyForecastItem(day: String, desc: String, high: String, low: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = day,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(android.R.drawable.ic_menu_compass),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = desc,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            Text(
                text = high,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = low,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}