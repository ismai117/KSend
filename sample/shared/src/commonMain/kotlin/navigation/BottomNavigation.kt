package navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator


sealed class BottomNavigationItems(val route: String, val label: String, val iconSelected: ImageVector, val iconUnselected: ImageVector) {
    object EMAIL : BottomNavigationItems(route = "email_screen", label = "EMAIL", iconSelected = Icons.Default.Email, iconUnselected = Icons.Outlined.Email)

    object SMS : BottomNavigationItems(route = "sms_screen", label = "SMS", iconSelected = Icons.Default.Sms, iconUnselected = Icons.Outlined.Sms)
}

private val screens = listOf(
    BottomNavigationItems.EMAIL,
    BottomNavigationItems.SMS
)

@Composable
fun BottomNavigation(
    navigator: Navigator
) {
    NavigationBar {

        val navBackStackEntry by navigator.currentEntry.collectAsStateWithLifecycle(null)
        val currentDestination = navBackStackEntry?.route?.route

        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination == screen.route,
                onClick = {
                    navigator.navigate(
                        route = screen.route,
                        NavOptions(
                            launchSingleTop = true
                        )
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (currentDestination == screen.route) screen.iconSelected else screen.iconUnselected,
                        contentDescription = screen.label
                    )
                },
                label = {
                    Text(
                        text = screen.label
                    )
                }
            )
        }

    }
}