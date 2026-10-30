package com.pnc.jetpackcomposedemos.core.structure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pnc.jetpackcomposedemos.ui.theme.LocalSpacing

// used for the screens shown before sign-in.
// they handle every system bar for themselves
// and add the side margins
@Composable
fun FullScreenBox(content: @Composable () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = LocalSpacing.current.large)
    ) {
        content()
    }

}



