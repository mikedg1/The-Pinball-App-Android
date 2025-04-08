package com.mikedg.thepinballapp.features.home.view

import PhotoScore
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mikedg.thepinballapp.features.about.view.AboutScreen
import com.mikedg.thepinballapp.features.changelog.ChangeLogViewModel
import com.mikedg.thepinballapp.features.changelog.view.ChangeLogScreen
import com.mikedg.thepinballapp.features.home.Route
import com.mikedg.thepinballapp.features.machinedetail.MachineDetailViewModel
import com.mikedg.thepinballapp.features.machinedetail.view.MachineDetailScreen
import com.mikedg.thepinballapp.features.search.view.SearchScreen
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
            NavHost(navController = navController, startDestination = Route.Search) {
                composable<Route.Search> {
                    SearchScreen(navController, innerPadding)
                }
                composable<Route.ChangeLog> {
                    val changeLogViewModel: ChangeLogViewModel = hiltViewModel()
                    ChangeLogScreen(changeLogViewModel, navController, innerPadding)
                }
                composable<Route.MachineInfo> {
                    val machineDetails: Route.MachineInfo = it.toRoute()

                    val extras = MutableCreationExtras().apply {
                        set(MachineDetailViewModel.OPDB_MACHINE_ID_KEY, machineDetails.id)
                    }
                    val machineDetailsViewModel: MachineDetailViewModel = viewModel(
                        factory = MachineDetailViewModel.Factory,
                        extras = extras,
                    )
                    // TODO: implement progress LCE status
                    val machineInfo by machineDetailsViewModel.machine.collectAsState()
                    machineInfo?.let { machineInfo ->
                        MachineDetailScreen(machineInfo, innerPadding)
                    } ?: Text("Machine Details ${machineDetails.id}", modifier = Modifier.padding(innerPadding))
                }
                composable<Route.About> {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AboutScreen()
                    }
                }
                composable<Route.TakePhoto> {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        PhotoScore()
                    }
                }
            }
        }
    }
}