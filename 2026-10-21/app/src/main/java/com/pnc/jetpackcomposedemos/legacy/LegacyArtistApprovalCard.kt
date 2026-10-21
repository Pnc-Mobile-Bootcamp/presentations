package com.pnc.jetpackcomposedemos.legacy

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.databinding.LegacyArtistApprovalBinding

@Composable
fun LegacyArtistApprovalCard(
    artist: Artist,
    isApproved: Boolean,
    onApprovalChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidViewBinding(
        factory = LegacyArtistApprovalBinding::inflate,
        modifier = modifier
    ) {
        artistNameText.text = artist.name
        genreText.text = "Genre: ${artist.genre}"
        locationText.text = artist.location

        description.text = artist.description

        approvalStatusText.text =
            if (isApproved) {
                "Approval status: Approved"
            } else {
                "Approval status: Pending"
            }

        approvalButton.text =
            if (isApproved) {
                "Revoke Approval"
            } else {
                "Approve Booking"
            }

        approvalButton.setOnClickListener {
            onApprovalChange(!isApproved)
        }

    }
}