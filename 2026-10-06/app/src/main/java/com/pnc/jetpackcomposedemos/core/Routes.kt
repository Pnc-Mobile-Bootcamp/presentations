package com.pnc.jetpackcomposedemos.core

import kotlinx.serialization.Serializable

sealed interface EventPlannerRoute

@Serializable
data object DashboardRoute: EventPlannerRoute

@Serializable
data object ArtistListRoute: EventPlannerRoute

@Serializable
data class ArtistDetailsRoute(
    val artistId: Int
): EventPlannerRoute


@Serializable
data object BoardMemberListRoute: EventPlannerRoute

@Serializable
data class BoardMemberDetailsRoute(
    val boardMemberId: Int
): EventPlannerRoute



@Serializable
data object LegacyArtistListRoute: EventPlannerRoute


@Serializable
data object OrdersRoute: EventPlannerRoute






