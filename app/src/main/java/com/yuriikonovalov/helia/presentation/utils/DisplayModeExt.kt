package com.yuriikonovalov.helia.presentation.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.HotelDisplayMode
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

@SuppressLint("ComposableNaming")
@Composable
fun HotelDisplayMode.getTintWithCurrentMode(
    currentHotelDisplayMode: HotelDisplayMode
) = if (this == currentHotelDisplayMode) {
    HeliaTheme.colors.primary500
} else if (HeliaTheme.theme.isDark) {
    HeliaTheme.colors.white
} else {
    HeliaTheme.colors.greyscale900
}


fun HotelDisplayMode.getIconResWithCurrentMode(currentHotelDisplayMode: HotelDisplayMode): Int {
    return when (this) {
        HotelDisplayMode.LIST ->
            if (this == currentHotelDisplayMode) R.drawable.ic_document else R.drawable.ic_document_border

        HotelDisplayMode.GRID ->
            if (this == currentHotelDisplayMode) R.drawable.ic_category else R.drawable.ic_category_border
    }
}