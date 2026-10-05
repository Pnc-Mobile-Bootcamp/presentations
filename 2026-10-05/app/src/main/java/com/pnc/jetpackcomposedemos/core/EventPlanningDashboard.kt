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
import androidx.compose.ui.unit.dp

@Composable
fun EventPlanningDashboard(
    onViewArtists: () -> Unit,
    onViewBoardMembers: () -> Unit,
    onLegacyArtists: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Event Planning Operations",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = onViewArtists,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Available Artists")
        }

        Button(
            onClick = onViewBoardMembers,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Board Members")
        }

//        Button(
//            onClick = onLegacyArtists,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("View Legacy Artists")
//        }

    }

}

