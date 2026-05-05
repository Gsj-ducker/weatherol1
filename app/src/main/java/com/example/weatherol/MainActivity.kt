package com.example.weatherol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weatherol.ui.city.CityScreen
import com.example.weatherol.ui.forecast.ForecastScreen
import com.example.weatherol.ui.home.HomeScreen
import com.example.weatherol.ui.settings.SettingsScreen
import com.example.weatherol.ui.theme.WeatherolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navItems = listOf(
        NavItem("首页", Icons.Default.Home),
        NavItem("预报", Icons.Default.LocationOn),
        NavItem("城市", Icons.Default.Add),
        NavItem("设置", Icons.Default.Settings)
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    // ======================
    // 全局状态：选中的城市经纬度
    // ======================
    var selectedLat by remember { mutableStateOf(39.9042) }
    var selectedLon by remember { mutableStateOf(116.4074) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(item.icon, contentDescription = item.label)
                        },
                        label = {
                            Text(item.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedIndex) {
                // 首页：传入选中的城市经纬度
                0 -> HomeScreen(
                    latitude = selectedLat,
                    longitude = selectedLon
                )

                1 -> ForecastScreen()

                // 城市页面：传入回调，选中后更新全局坐标
                2 -> CityScreen(
                    onCitySelected = { cityName, lat, lon ->
                        selectedLat = lat
                        selectedLon = lon
                        selectedIndex = 0 // 选完自动跳回首页
                    }
                )

                3 -> SettingsScreen()
            }
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector
)