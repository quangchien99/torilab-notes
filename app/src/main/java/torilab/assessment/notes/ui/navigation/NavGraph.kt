package torilab.assessment.notes.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import torilab.assessment.notes.ui.screen.history.navigation.historyScreen
import torilab.assessment.notes.ui.screen.home.navigation.Home
import torilab.assessment.notes.ui.screen.home.navigation.homeScreen
import torilab.assessment.notes.ui.screen.settings.navigation.settingsScreen
import torilab.assessment.notes.ui.theme.LightGray200
import torilab.assessment.notes.ui.theme.NeutralBlack
import torilab.assessment.notes.ui.theme.NeutralWhite
import torilab.assessment.notes.ui.theme.LightGray400

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Check if current destination is a bottom nav destination
    val isBottomNavDestination = BottomNav.entries.any { bottomNav ->
        currentDestination?.hierarchy?.any {
            it.hasRoute(bottomNav.route::class)
        } == true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomNavDestination) {
                NavigationBar(
                    containerColor = NeutralWhite,
                ) {
                    BottomNav.entries.forEach { bottomNav ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.hasRoute(bottomNav.route::class)
                        } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(bottomNav.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = bottomNav.iconId),
                                    contentDescription = stringResource(id = bottomNav.titleTextId)
                                )
                            },
                            label = {
                                Text(text = stringResource(id = bottomNav.titleTextId))
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeutralBlack,
                                selectedTextColor = NeutralBlack,
                                unselectedIconColor = LightGray400,
                                unselectedTextColor = LightGray400,
                                indicatorColor = LightGray200,
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (isBottomNavDestination) {

            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            homeScreen()
            historyScreen()
            settingsScreen()
        }
    }
}
