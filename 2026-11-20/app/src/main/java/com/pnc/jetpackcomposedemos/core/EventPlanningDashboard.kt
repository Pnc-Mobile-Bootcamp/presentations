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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.R

@Composable
fun EventPlanningDashboard(
    onViewArtists: () -> Unit,
    onViewBoardMembers: () -> Unit,
    onViewOrders: () -> Unit,
    onViewToDos: () -> Unit,
    onSignOut: () -> Unit,
    onSettings: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.dashboard_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag("dashboard-title")
        )


        Button(
            onClick = onViewBoardMembers,
            modifier = Modifier.fillMaxWidth()
                .testTag("view-board-button")
        ) {
            Text(stringResource(R.string.dashboard_view_board_members))
        }


        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth()
                .testTag("sign-out-button")
        ) {
            Text(stringResource(R.string.dashboard_sign_out))
        }

    }

}

