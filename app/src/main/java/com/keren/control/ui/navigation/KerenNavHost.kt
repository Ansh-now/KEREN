package com.keren.control.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keren.control.ui.screens.devices.DevicesScreen
import com.keren.control.ui.screens.logs.LogsScreen
import com.keren.control.ui.screens.nervous.NervousSystemScreen
import com.keren.control.ui.screens.overview.OverviewScreen
import com.keren.control.ui.screens.settings.SettingsScreen
import com.keren.control.ui.screens.tasks.TasksScreen
import com.keren.control.ui.screens.terminal.TerminalScreen
import com.keren.control.ui.theme.KerenBlue
import com.keren.control.ui.theme.KerenSurface
import com.keren.control.ui.theme.KerenTextDim

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Overview : Screen("overview", "Overview", Icons.Default.Home)
    data object Devices : Screen("devices", "Devices", Icons.Default.Devices)
    data object Tasks : Screen("tasks", "Tasks", Icons.Default.List)
    data object Terminal : Screen("terminal", "Terminal", Icons.Default.Terminal)
    data object Nervous : Screen("nervous", "Nervous", Icons.Outlined.Hub)
    data object Logs : Screen("logs", "Logs", Icons.Default.List)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

// Settings in bottom bar so Core URL / CONNECT is always reachable
val bottomBarScreens = listOf(
    Screen.Overview,
    Screen.Devices,
    Screen.Terminal,
    Screen.Nervous,
    Screen.Settings
)

@Composable
fun KerenNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = KerenSurface
            ) {
                bottomBarScreens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = KerenBlue,
                            selectedTextColor = KerenBlue,
                            unselectedIconColor = KerenTextDim,
                            unselectedTextColor = KerenTextDim,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Overview.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Overview.route) { OverviewScreen() }
            composable(Screen.Devices.route) { DevicesScreen() }
            composable(Screen.Tasks.route) { TasksScreen() }
            composable(Screen.Terminal.route) { TerminalScreen() }
            composable(Screen.Nervous.route) { NervousSystemScreen() }
            composable(Screen.Logs.route) { LogsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}
