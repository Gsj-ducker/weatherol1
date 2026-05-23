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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherol.ui.city.CityScreen
import com.example.weatherol.ui.forecast.ForecastScreen
import com.example.weatherol.ui.home.HomeScreen
import com.example.weatherol.ui.settings.SettingsScreen
import com.example.weatherol.ui.settings.SettingsViewModel
import com.example.weatherol.ui.settings.AboutScreen
import com.example.weatherol.ui.settings.HelpScreen
import com.example.weatherol.ui.theme.WeatherolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

            WeatherolTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(settingsViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()

    // 当前选中的底部导航项
    var selectedIndex by remember { mutableStateOf(0) }

    // 监听导航变化，更新选中状态
    navController.addOnDestinationChangedListener { _, destination, _ ->
        selectedIndex = when (destination.route) {
            "home" -> 0
            "forecast" -> 1
            "city" -> 2
            "settings" -> 3
            else -> selectedIndex
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navItems = listOf(
                    NavItem("首页", Icons.Default.Home, "home"),
                    NavItem("预报", Icons.Default.LocationOn, "forecast"),
                    NavItem("城市", Icons.Default.Add, "city"),
                    NavItem("设置", Icons.Default.Settings, "settings")
                )

                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") { HomeScreen() }
                composable("forecast") { ForecastScreen() }
                composable("city") { CityScreen() }
                composable("settings") { SettingsScreen(settingsViewModel = settingsViewModel, navController = navController) }
                composable("about") { AboutScreen(navController) }
                composable("help") { HelpScreen(navController) }
            }
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)