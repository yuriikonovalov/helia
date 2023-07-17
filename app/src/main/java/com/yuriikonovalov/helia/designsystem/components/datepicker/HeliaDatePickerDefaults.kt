package com.yuriikonovalov.helia.designsystem.components.datepicker

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme

object HeliaDatePickerDefaults {
    val contentColor
        @Composable
        get() = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors() = DatePickerDefaults.colors(
        containerColor = HeliaTheme.backgroundColor,
        weekdayContentColor = contentColor,
        yearContentColor = contentColor,
        currentYearContentColor = contentColor,
        selectedYearContentColor = HeliaTheme.colors.white,
        selectedYearContainerColor = HeliaTheme.colors.primary500,
        subheadContentColor = contentColor,
        todayDateBorderColor = HeliaTheme.colors.primary500,
        dayContentColor = contentColor,
        todayContentColor = contentColor,
        selectedDayContainerColor = HeliaTheme.colors.primary500,
        selectedDayContentColor = HeliaTheme.colors.white
    )
}