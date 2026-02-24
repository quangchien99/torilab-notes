package torilab.assessment.notes.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import torilab.assessment.notes.R
import torilab.assessment.notes.ui.screen.addeditnote.navigation.addEditNoteScreen
import torilab.assessment.notes.ui.screen.addeditnote.navigation.navigateToAddEditNote
import torilab.assessment.notes.ui.screen.history.navigation.historyScreen
import torilab.assessment.notes.ui.screen.home.navigation.Home
import torilab.assessment.notes.ui.screen.home.navigation.homeScreen
import torilab.assessment.notes.ui.screen.settings.navigation.settingsScreen

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
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (isBottomNavDestination) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
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
                                selectedIconColor = MaterialTheme.colorScheme.onBackground,
                                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                unselectedIconColor = MaterialTheme.colorScheme.outline,
                                unselectedTextColor = MaterialTheme.colorScheme.outline,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            val isHome = currentDestination?.hasRoute(Home::class) == true
            if (isHome) {
                FloatingActionButton(
                    onClick = { navController.navigateToAddEditNote() },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_add),
                        contentDescription = stringResource(id = R.string.content_description_add_note)
                    )
                }
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
            addEditNoteScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
