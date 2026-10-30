package com.pnc.jetpackcomposedemos.core.structure

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pnc.jetpackcomposedemos.core.ArtistDetailsRoute
import com.pnc.jetpackcomposedemos.core.ArtistListRoute
import com.pnc.jetpackcomposedemos.core.BoardMemberDetailsRoute
import com.pnc.jetpackcomposedemos.core.BoardMemberListRoute
import com.pnc.jetpackcomposedemos.core.DashboardRoute
import com.pnc.jetpackcomposedemos.core.EventPlanningDashboard
import com.pnc.jetpackcomposedemos.core.OrdersRoute
import com.pnc.jetpackcomposedemos.core.SettingsRoute
import com.pnc.jetpackcomposedemos.core.ToDosRoute
import com.pnc.jetpackcomposedemos.core.settings.SettingsScreen
import com.pnc.jetpackcomposedemos.features.artists.presentation.composables.ArtistDetails
import com.pnc.jetpackcomposedemos.features.artists.presentation.composables.ArtistDirectoryRoute
import com.pnc.jetpackcomposedemos.features.boardmembers.domain.BoardMember
import com.pnc.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberDetails
import com.pnc.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberList
import com.pnc.jetpackcomposedemos.features.orders.presentation.OrdersScreen
import com.pnc.jetpackcomposedemos.features.todo.presentation.ToDosScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    useTwoPaneLayout: Boolean,
    onSignOut: () -> Unit
) {
    val boardMembers = BoardMember.getBoardMembers()

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

        composable<ArtistListRoute> {
            // load the artists from repository or service class

            ArtistDirectoryRoute(
                onArtistSelected = { artistId ->
                    navController.navigate(ArtistDetailsRoute(artistId))
                },
                onBack = {
                    navController.popBackStack()
                },
                useTwoPaneLayout = useTwoPaneLayout
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

        composable<ArtistDetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ArtistDetailsRoute>()
            val artistId = route.artistId

            ArtistDetails(artistId = artistId)

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
            OrdersScreen(useTwoPaneLayout = useTwoPaneLayout)
        }

        composable<ToDosRoute> {
            ToDosScreen(useTwoPaneLayout = useTwoPaneLayout)
        }

        composable<SettingsRoute> {
            SettingsScreen()
        }


    }


}