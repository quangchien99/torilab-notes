package torilab.assessment.notes.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import torilab.assessment.notes.R
import torilab.assessment.notes.common.Route
import torilab.assessment.notes.ui.screen.home.navigation.Home
import torilab.assessment.notes.ui.screen.settings.navigation.Settings

enum class BottomNav(
    val route: Route,
    @DrawableRes val iconId: Int,
    @StringRes val titleTextId: Int
) {
    HOME(
        Home,
        R.drawable.ic_outline_home,
        R.string.screen_title_home,
    ),
    SETTINGS(
        Settings,
        R.drawable.ic_baseline_settings,
        R.string.screen_title_settings
    )
}