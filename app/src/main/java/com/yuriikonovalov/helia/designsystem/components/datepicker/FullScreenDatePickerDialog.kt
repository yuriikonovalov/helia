package com.yuriikonovalov.helia.designsystem.components.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuriikonovalov.helia.designsystem.components.FullScreenDialog
import com.yuriikonovalov.helia.presentation.utils.toMillis
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDatePickerDialog(
    open: Boolean,
    onDismiss: () -> Unit,
    onSelect: (date: LocalDate) -> Unit,
    initialSelectedDate: LocalDate? = null
) {
    if (open) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDate?.toMillis(),
        )
        FullScreenDialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                DatePicker(
                    modifier = Modifier,
                    state = datePickerState,
                    colors = HeliaDatePickerDefaults.colors(),
                    title = null,
                    headline = null,
                    showModeToggle = false
                )
            }
        }

    }
}