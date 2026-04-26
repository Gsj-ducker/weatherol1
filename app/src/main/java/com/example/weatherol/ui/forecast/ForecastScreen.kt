package com.example.weatherol.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 模拟数据类
data class HourlyForecast(
    val time: String,
    val temp: String,
    val iconRes: Int // 示例：R.drawable.ic_sunny
)

data class DailyForecast(
    val day: String,
    val highTemp: String,
    val lowTemp: String,
    val iconRes: Int,
    val weatherDesc: String
)

@Composable
fun ForecastScreen() {
    // 模拟24小时预报数据
    val hourlyList = listOf(
        HourlyForecast("现在", "26°", android.R.drawable.ic_menu_compass),
        HourlyForecast("14:00", "27°", android.R.drawable.ic_menu_compass),
        HourlyForecast("15:00", "28°", android.R.drawable.ic_menu_compass),
        HourlyForecast("16:00", "27°", android.R.drawable.ic_menu_compass),
        HourlyForecast("17:00", "25°", android.R.drawable.ic_menu_compass),
        HourlyForecast("18:00", "23°", android.R.drawable.ic_menu_compass),
        HourlyForecast("19:00", "22°", android.R.drawable.ic_menu_compass),
        HourlyForecast("20:00", "21°", android.R.drawable.ic_menu_compass)
    )

    // 模拟7天预报数据
    val dailyList = listOf(
        DailyForecast("今天", "28°", "22°", android.R.drawable.ic_menu_compass, "晴"),
        DailyForecast("周二", "27°", "21°", android.R.drawable.ic_menu_compass, "多云"),
        DailyForecast("周三", "25°", "20°", android.R.drawable.ic_menu_compass, "小雨"),
        DailyForecast("周四", "24°", "19°", android.R.drawable.ic_menu_compass, "阴"),
        DailyForecast("周五", "26°", "20°", android.R.drawable.ic_menu_compass, "晴"),
        DailyForecast("周六", "27°", "21°", android.R.drawable.ic_menu_compass, "晴转多云"),
        DailyForecast("周日", "28°", "22°", android.R.drawable.ic_menu_compass, "晴")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8)) // 和首页风格统一的浅背景色
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "未来预报",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A202C),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 24小时预报模块（横向列表）
        HourlyForecastSection(hourlyList)

        Spacer(modifier = Modifier.height(24.dp))

        // 7天预报模块（纵向列表）
        DailyForecastSection(dailyList)
    }
}

// 24小时预报：横向列表
@Composable
fun HourlyForecastSection(hourlyList: List<HourlyForecast>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "24小时预报",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(hourlyList) { forecast ->
                    HourlyForecastItem(forecast)
                }
            }
        }
    }
}

@Composable
fun HourlyForecastItem(forecast: HourlyForecast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = forecast.time,
            fontSize = 14.sp,
            color = Color(0xFF718096)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = forecast.iconRes),
            contentDescription = "天气图标",
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = forecast.temp,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D3748)
        )
    }
}

// 7天预报：纵向列表
@Composable
fun DailyForecastSection(dailyList: List<DailyForecast>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "7天预报",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3748),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            dailyList.forEachIndexed { index, forecast ->
                DailyForecastItem(forecast)
                if (index != dailyList.lastIndex) {
                    Divider(
                        color = Color(0xFFE2E8F0),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DailyForecastItem(forecast: DailyForecast) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 日期
        Text(
            text = forecast.day,
            fontSize = 14.sp,
            color = Color(0xFF2D3748),
            modifier = Modifier.weight(1f)
        )

        // 天气图标 + 描述
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = forecast.iconRes),
                contentDescription = "天气图标",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = forecast.weatherDesc,
                fontSize = 14.sp,
                color = Color(0xFF718096)
            )
        }

        // 温度范围
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = forecast.highTemp,
                fontSize = 14.sp,
                color = Color(0xFF2D3748),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = forecast.lowTemp,
                fontSize = 14.sp,
                color = Color(0xFF718096)
            )
        }
    }
}