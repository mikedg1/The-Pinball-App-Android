package com.mikedg.thepinballapp.features.home.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mikedg.thepinballapp.features.home.Route
import com.mikedg.thepinballapp.ui.theme.ThePinballAppTheme

@Composable
internal fun currentRoute(navController: NavHostController): Route? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val routePath = navBackStackEntry?.destination?.route ?: return null

    // TODO: simplify, would reflection be ok here so we don't miss adding something
    return when (routePath) {
        Route.Search::class.qualifiedName -> Route.Search
        Route.ChangeLog::class.qualifiedName -> Route.ChangeLog
        Route.About::class.qualifiedName -> Route.About
        else -> null
    }
}

@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    ThePinballAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),

            bottomBar = {
                BottomNavBar(navController = navController)
            }) { innerPadding ->
            NavHost(navController = navController, startDestination = Route.ChangeLog) {
                composable<Route.Search> {
                    Text("Search")
                }
                composable<Route.ChangeLog> {
                    Text("Change Log")
                }
                composable<Route.MachineInfo> {
                    Text("Machine Info: ${it.id}")
                }
                composable<Route.About> {
                    Text("About")
                }
            }
        }
    }
}