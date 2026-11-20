package com.pnc.jetpackcomposedemos.core.structure

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pnc.jetpackcomposedemos.core.ArtistListRoute
import com.pnc.jetpackcomposedemos.core.BoardMemberDetailsRoute
import com.pnc.jetpackcomposedemos.core.BoardMemberListRoute
import com.pnc.jetpackcomposedemos.core.DashboardRoute
import com.pnc.jetpackcomposedemos.core.EventPlanningDashboard
import com.pnc.jetpackcomposedemos.core.OrdersRoute
import com.pnc.jetpackcomposedemos.core.SettingsRoute
import com.pnc.jetpackcomposedemos.core.ToDosRoute
import com.pnc.jetpackcomposedemos.core.settings.SettingsScreen
import com.pnc.jetpackcomposedemos.features.artists.presentation.composables.ArtistDirectoryRoute
import com.pnc.jetpackcomposedemos.features.boardmembers.domain.BoardMember
import com.pnc.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberDetails
import com.pnc.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberList
import com.pnc.jetpackcomposedemos.features.orders.presentation.PagedOrdersScreen
import com.pnc.jetpackcomposedemos.features.todo.presentation.ToDosScreen
import androidx.core.util.Consumer
import android.content.Intent
import androidx.navigation.navDeepLink
import com.pnc.jetpackcomposedemos.core.sync.ArtistSyncNotifier

@Composable
fun AppNavHost(
    navController: NavHostController,
    useTwoPaneLayout: Boolean,
    onSignOut: () -> Unit
) {
    val boardMembers = BoardMember.getBoardMembers()

    // MainActivity is a single task, so a link tapped while the app is open
    // arrives as a NEW intent
    // And our NavHost reads the original launch intent by itself, but not
    // these later ones
    val activity = LocalActivity.current as? ComponentActivity
    DisposableEffect(activity, navController) {
        val listener = Consumer<Intent> { intent -> navController.handleDeepLink(intent) }
        activity?.addOnNewIntentListener(listener)
        onDispose { activity?.removeOnNewIntentListener(listener) }
    }

    NavHost(
        navController = navController,
        startDestination = DashboardRoute
    ) {
        composable<DashboardRoute> {
            EventPlanningDashboard(
                onViewArtists = {
                    navController.navigate(ArtistListRoute)
                },
                onViewBoardMembers = {
                    navController.navigate(BoardMemberListRoute)
                },
                onViewOrders = {
                    navController.navigate(OrdersRoute)
                },
                onViewToDos = {
                    navController.navigate(ToDosRoute)
                },
                onSignOut = onSignOut,
                onSettings = {
                    navController.navigate(SettingsRoute)
                }
            )
        }

        composable<ArtistListRoute>(
            deepLinks = listOf(
                navDeepLink<ArtistListRoute>(basePath = ArtistSyncNotifier.ARTISTS_DEEP_LINK)
            )
        ) {
            // load the artists from repository or service class

            ArtistDirectoryRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<BoardMemberListRoute> {
            BoardMemberList(
                members = boardMembers,
                onMemberSelected = { memberId ->
                    navController.navigate(BoardMemberDetailsRoute(memberId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<BoardMemberDetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<BoardMemberDetailsRoute>()
            val memberId = route.boardMemberId

            val member = boardMembers.find { it.id == memberId }

            if (member != null) {
                BoardMemberDetails(boardMember = member)
            } else {
                Text("Board Member not found")
            }
        }

        composable<OrdersRoute> {
            PagedOrdersScreen(useTwoPaneLayout = useTwoPaneLayout)
        }

        composable<ToDosRoute> {
            ToDosScreen(useTwoPaneLayout = useTwoPaneLayout)
        }

        composable<SettingsRoute> {
            SettingsScreen()
        }


    }


}