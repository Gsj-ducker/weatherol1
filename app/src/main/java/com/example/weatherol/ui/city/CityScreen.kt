package com.example.weatherol.ui.city

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 统一数据类
data class City(
    val id: Int,
    val name: String,
    val weatherText: String = "多云"
)

@Composable
fun CityScreen() {
    var searchText by remember { mutableStateOf("") }

    // 默认城市列表
    val cityList = remember {
        mutableStateListOf(
            City(1, "北京", "晴天"),
            City(2, "上海", "多云"),
            City(3, "广州", "小雨"),
            City(4, "深圳", "晴"),
            City(5, "杭州", "阴"),
            City(6, "成都", "雾"),
            City(7, "重庆", "多云"),
            City(8, "武汉", "雨"),
            City(9, "西安", "晴"),
            City(10, "南京", "多云"),
            City(11, "天津", "晴"),
            City(12, "苏州", "多云"),
            City(13, "乌鲁木齐", "晴")
        )
    }

    // 搜索过滤
    val filteredList = remember(searchText, cityList) {
        if (searchText.isBlank()) cityList
        else cityList.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "城市管理",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("搜索城市") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(16.dp))

        Text(
            "已添加城市 (${filteredList.size})",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredList, key = { it.id }) { city ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CityCard(city) {
                        cityList.remove(city)
                    }
                }
            }
        }
    }
}

@Composable
fun CityCard(city: City, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                city.name,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Text(
                city.weatherText,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.width(12.dp))

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}