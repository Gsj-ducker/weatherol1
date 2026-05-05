package com.example.weatherol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.weatherol.ui.city.CityScreen
import com.example.weatherol.ui.forecast.ForecastScreen
import com.example.weatherol.ui.home.HomeScreen
import com.example.weatherol.ui.setting.SettingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableStateOf(0) }

    // ✅ 顺序完全按你要求：首页 → 预报 → 城市 → 设置
    val items = listOf(
        "首页" to Icons.Default.Home,
        "预报" to Icons.Default.List,
        "城市" to Icons.Default.Place,
        "设置" to Icons.Default.Settings
    )

    var selectedLat by remember { mutableStateOf(39.9042) }
    var selectedLon by remember { mutableStateOf(116.4074) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.second, null) },
                        label = { Text(item.first) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppState.themeColor.value,
                            selectedTextColor = AppState.themeColor.value
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedIndex) {
                0 -> HomeScreen(selectedLat, selectedLon)      // 1. 首页
                1 -> ForecastScreen(selectedLat, selectedLon)  // 2. 预报
                2 -> CityScreen { _, lat, lon ->               // 3. 城市
                    selectedLat = lat
                    selectedLon = lon
                    selectedIndex = 0 // 选完城市 → 自动跳回首页
                }
                3 -> SettingScreen()                           // 4. 设置
            }
        }
    }
}