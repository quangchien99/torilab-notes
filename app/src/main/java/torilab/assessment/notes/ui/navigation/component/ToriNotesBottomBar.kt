package torilab.assessment.notes.ui.navigation.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import torilab.assessment.notes.ui.navigation.BottomNav
import torilab.assessment.notes.ui.theme.ToriNotesTheme

@Composable
internal fun ToriNotesBottomBar(
    selectedRoute: BottomNav?,
    onItemClick: (BottomNav) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        BottomNav.entries.forEach { bottomNav ->
            NavigationBarItem(
                selected = bottomNav == selectedRoute,
                onClick = { onItemClick(bottomNav) },
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

@Preview(showBackground = true)
@Composable
private fun ToriNotesBottomBarHomePreview() {
    ToriNotesTheme(dynamicColor = false) {
        ToriNotesBottomBar(
            selectedRoute = BottomNav.HOME,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ToriNotesBottomBarSettingsPreview() {
    ToriNotesTheme(dynamicColor = false) {
        ToriNotesBottomBar(
            selectedRoute = BottomNav.SETTINGS,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ToriNotesBottomBarDarkPreview() {
    ToriNotesTheme(dynamicColor = false, darkTheme = true) {
        ToriNotesBottomBar(
            selectedRoute = BottomNav.HOME,
            onItemClick = {}
        )
    }
}
