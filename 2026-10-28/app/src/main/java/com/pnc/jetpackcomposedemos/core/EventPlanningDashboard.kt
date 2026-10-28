package com.pnc.jetpackcomposedemos.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun EventPlanningDashboard(
    onViewArtists: () -> Unit,
    onViewBoardMembers: () -> Unit,
    onViewOrders: () -> Unit,
    onViewToDos: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Event Planning Operations",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag("dashboard-title")
        )

        Button(
            onClick = onViewArtists,
            modifier = Modifier.fillMaxWidth()
                .testTag("view-artists-button")
        ) {
            Text("View Available Artists")
        }

        Button(
            onClick = onViewBoardMembers,
            modifier = Modifier.fillMaxWidth()
                .testTag("view-board-button")
        ) {
            Text("View Board Members")
        }

        Button(
            onClick = onViewOrders,
            modifier = Modifier.fillMaxWidth()
                .testTag("view-order-button")
        ) {
            Text("View Orders")
        }

        Button(
            onClick = onViewToDos,
            modifier = Modifier.fillMaxWidth()
                .testTag("view-todo-button")
        ) {
            Text("View To-Dos")
        }

        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth()
                .testTag("sign-out-button")
        ) {
            Text("Sign out")
        }

    }

}

