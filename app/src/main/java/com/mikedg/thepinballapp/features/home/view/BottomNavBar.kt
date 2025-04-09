package com.mikedg.thepinballapp.features.home.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.mikedg.thepinballapp.features.home.Route

data class BottomNavItem(val title: String, val route: Route, val icon: ImageVector)

private object Screen {
    val About = BottomNavItem("About", Route.About, Icons.Default.Info)
    val ChangeLog = BottomNavItem("Changelog", Route.ChangeLog, Icons.AutoMirrored.Filled.List)
    val Search = BottomNavItem("Search", Route.Search, Icons.Default.Search)
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Screen.Search,
        Screen.ChangeLog,
        Screen.About,
    )
//    BottomAppBar{// = CircleShape) {

    BottomAppBar {
        val currentRoute = currentRoute(navController)

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = screen.title)
                },
                label = {
                    Text(text = screen.title)
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId)
                        }
                    }
                }
            )
        }
        FloatingActionButton(onClick = { navController.navigate(Route.TakePhoto) }) {
            Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Capture Score")
        }
    }
}