package com.caloriesai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.caloriesai.ui.theme.CaloriesAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaloriesAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppRoot()
                }
            }
        }
    }
}

private data class BottomItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomItems = listOf(
    BottomItem("diary", "سجلي الغذائي", Icons.Filled.Book),
    BottomItem("scan", "مسح الطعام", Icons.Filled.CameraAlt),
    BottomItem("coach", "المدرب الذكي", Icons.Filled.Psychology),
    BottomItem("settings", "الإعدادات", Icons.Filled.Settings)
)

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, fontSize = 11.sp) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "diary",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("diary") { DiaryScreen() }
            composable("scan") { ScanScreen() }
            composable("coach") { CoachScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

@Composable
fun ScreenContainer(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(72.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(subtitle, fontSize = 14.sp)
    }
}

@Composable
fun DiaryScreen() = ScreenContainer(
    "سجلي الغذائي",
    "هنا تظهر وجباتك اليومية والسعرات الحرارية",
    Icons.Filled.Book
)

@Composable
fun ScanScreen() = ScreenContainer(
    "مسح الطعام",
    "استخدم الكاميرا لمسح طبق الطعام وحساب سعراته",
    Icons.Filled.CameraAlt
)

@Composable
fun CoachScreen() = ScreenContainer(
    "المدرب الذكي",
    "نصائح غذائية مخصصة حسب هدفك",
    Icons.Filled.Psychology
)

@Composable
fun SettingsScreen() = ScreenContainer(
    "الإعدادات",
    "إدارة حسابك وتفضيلات التطبيق",
    Icons.Filled.Settings
)
