package com.pnc.jetpackcomposedemos.core.structure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pnc.jetpackcomposedemos.ui.theme.LocalSpacing

@Composable
fun AppNavigationShell(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val currentDestination = navController
        .currentBackStackEntryAsState().value?.destination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { destination ->
                item(
                    selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(destination.route::class)
                    } == true,
                    onClick = {
                        navController.navigate(destination.route) {
                            // one back stack per tab:
                            // leave a tab, come back, find it as you left it
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(destination.icon, contentDescription = null) },
                    label = { Text(stringResource(destination.labelRes))}
                )
            }
        }
    ) {
        // The nav bar handles its system insets
        // but we need to provide space for them (and side margins)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = LocalSpacing.current.large)
        ) {
            content()
        }
    }


}



