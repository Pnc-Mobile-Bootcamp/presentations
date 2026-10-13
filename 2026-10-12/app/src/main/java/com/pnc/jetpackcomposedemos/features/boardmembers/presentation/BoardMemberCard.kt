package com.pnc.jetpackcomposedemos.features.boardmembers.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.features.boardmembers.domain.BoardMember
import com.pnc.jetpackcomposedemos.ui.theme.JetpackComposeDemosTheme

@Composable
fun BoardMemberCard(
    member: BoardMember,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${member.firstName} ${member.lastName}",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Title: ${member.title}")
        }
    }

}


@Preview(showBackground = false)
@Composable
fun BoardMemberCardPreview() {
    val member = BoardMember(
        id = 1,
        firstName = "Morgan",
        lastName = "Sloan",
        title = "CEO",
        imageUrl = "/images/MorganSloan.jpg",
        bio = "Some impressive stuff"
    )

    JetpackComposeDemosTheme {
        BoardMemberCard(
            member = member
        )
    }
}

