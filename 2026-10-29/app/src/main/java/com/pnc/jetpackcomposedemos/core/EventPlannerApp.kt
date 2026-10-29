package com.pnc.jetpackcomposedemos.core

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowSizeClass
import com.pnc.jetpackcomposedemos.features.artists.presentation.composables.ArtistDetails
import com.pnc.jetpackcomposedemos.features.artists.presentation.composables.ArtistDirectoryRoute
import com.pnc.jetpackcomposedemos.features.auth.domain.AuthState
import com.pnc.jetpackcomposedemos.features.auth.presentation.AuthViewModel
import com.pnc.jetpackcomposedemos.features.auth.presentation.SignInScreen
import com.pnc.jetpackcomposedemos.features.boardmembers.domain.BoardMember
import com.pnc.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberDetails
import com.pnc.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberList
import com.pnc.jetpackcomposedemos.features.orders.presentation.OrdersScreen
import com.pnc.jetpackcomposedemos.features.todo.presentation.ToDosScreen

@Composable
fun EventPlannerApp(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.uiState.collectAsStateWithLifecycle()

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val useTwoPaneLayout = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    val navController = rememberNavController()
    val boardMembers = BoardMember.getBoardMembers()

    when (authState) {
        is AuthState.Restoring -> CircularProgressIndicator()
        is AuthState.Unauthenticated -> {
            SignInScreen(
                onSignIn = { user -> viewModel.login(user) }
            )
        }
        is AuthState.Authenticated -> {
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
                        onSignOut = {
                            viewModel.logout()
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


            }
        }
    }

}



