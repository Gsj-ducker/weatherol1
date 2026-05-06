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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherol.AppState
import com.example.weatherol.data.common.DataResult
import com.example.weatherol.data.remote.model.WeatherResponse
import com.example.weatherol.data.repository.WeatherRepository

data class HourlyItem(val time: String, val temp: String)
data class DailyItem(val day: String, val max: String, val min: String)

@Composable
fun ForecastScreen(lat: Double = 39.9042, lon: Double = 116.4074) {
    val repo = remember { WeatherRepository() }
    val state = remember { androidx.compose.runtime.mutableStateOf<DataResult<WeatherResponse>?>(null) }

    LaunchedEffect(lat, lon) {
        state.value = repo.fetchWeather(lat, lon)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("未来预报", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))

        when (val current = state.value) {
            is DataResult.Success -> {
                val data = current.data
                val hourly = data.hourly
                val daily = data.daily

                val hList = mutableListOf<HourlyItem>()
                repeat(8) { i ->
                    val t = hourly?.time?.getOrNull(i)?.takeLast(5) ?: ""
                    val temp = hourly?.temperature2m?.getOrNull(i) ?: 0.0
                    val s = if (AppState.isCelsius.value) "${temp.toInt()}℃" else "${(temp*1.8+32).toInt()}℉"
                    hList.add(HourlyItem(t, s))
                }

                Text("24小时预报", fontSize = 16.sp)
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(hList) {
                        Card(
                            modifier = Modifier.width(80.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(Color.White)
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(it.time, fontSize = 13.sp, color = Color.Gray)
                                Spacer(Modifier.height(8.dp))
                                Text(it.temp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(30.dp))
                Text("7天预报", fontSize = 16.sp)
                Spacer(Modifier.height(12.dp))

                val days = listOf("今天","明天","周三","周四","周五","周六","周日")
                for (i in days.indices) {
                    val max = daily?.temperature2mMax?.getOrNull(i) ?: 0.0
                    val min = daily?.temperature2mMin?.getOrNull(i) ?: 0.0
                    val maxStr = if (AppState.isCelsius.value) "${max.toInt()}℃" else "${(max*1.8+32).toInt()}℉"
                    val minStr = if (AppState.isCelsius.value) "${min.toInt()}℃" else "${(min*1.8+32).toInt()}℉"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(days[i], fontWeight = FontWeight.Medium)
                            Text("$maxStr / $minStr")
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
            is DataResult.Error -> Text("错误: ${current.message}", color = Color.Red)
            else -> CircularProgressIndicator(color = AppState.themeColor.value)
        }
    }
}