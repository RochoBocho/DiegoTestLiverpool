package com.example.diegorochintest.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun ColorCircle(
    colorHex: String,
    modifier: Modifier = Modifier,
) {
    val color =
        try {
            Color(colorHex.toColorInt())
        } catch (e: Exception) {
            Color.Gray
        }

    Box(
        modifier =
            modifier
                .size(24.dp)
                .clip(CircleShape)
                .border(0.5.dp, Color.Gray, CircleShape)
                .padding(2.dp),
    ) {
        Surface(
            modifier = Modifier.size(20.dp),
            shape = CircleShape,
            color = color,
        ) { }
    }
}
