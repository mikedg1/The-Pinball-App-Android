package com.mikedg.thepinballapp.features.home.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.mikedg.thepinballapp.features.home.Route

data class BottomNavItem(val title: String, val route: Route, val icon: ImageVector)

private object Screen {
    val About = BottomNavItem("About", Route.About, Icons.Default.Info)
    val ChangeLog = BottomNavItem("Changelog", Route.ChangeLog, Icons.Default.Home)
    val Search = BottomNavItem("Search", Route.Search, Icons.Default.Search)
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Screen.Search,
        Screen.About,
        Screen.ChangeLog,
    )

    NavigationBar {
        val currentRoute = currentRoute(navController)

        items.forEach { screen ->
            NavigationBarItem(icon = {
                Icon(imageVector = screen.icon, contentDescription = screen.title)
            }, label = {
                Text(text = screen.title)
            }, selected = currentRoute == screen.route, onClick = {
                if (currentRoute != screen.route) {
                    navController.navigate(screen.route) {
                        // Avoid creating multiple copies of the same destination
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            })
        }
    }
}