package com.weather.weatherforecastapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.noRippleClickable(
    isEnabled: Boolean = true, onClick: () -> Unit
): Modifier = composed {
    this.then(
        Modifier.clickable(
            indication = null,
            enabled = isEnabled,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        })

}