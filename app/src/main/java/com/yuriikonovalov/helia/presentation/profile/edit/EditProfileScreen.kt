package com.yuriikonovalov.helia.presentation.profile.edit

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.InputField
import com.yuriikonovalov.helia.designsystem.components.InputFieldDefaults
import com.yuriikonovalov.helia.designsystem.components.LoadingDialog
import com.yuriikonovalov.helia.designsystem.components.Navbar
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.datepicker.DatePickerDialog
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.domain.valueobjects.Gender
import com.yuriikonovalov.helia.presentation.utils.nameStringResId
import com.yuriikonovalov.helia.presentation.utils.toUiString
import java.time.LocalDate

@Composable
fun EditProfileScreen(
    onNavigateClick: () -> Unit,
    viewModel: EditProfileScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current


    EditProfileScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        state = state,
        onNavigateClick = onNavigateClick,
        onUpdateClick = { viewModel.handleIntent(EditProfileIntent.Update) },
        onFullNameChanged = { viewModel.handleIntent(EditProfileIntent.InputFullName(it)) },
        onDateChanged = { viewModel.handleIntent(EditProfileIntent.UpdateDateOfBirth(it)) },
        onGenderChanged = { viewModel.handleIntent(EditProfileIntent.UpdateGender(it)) }
    )

    if (state.isProfileUpdated) {
        LaunchedEffect(Unit) {
            onNavigateClick()
        }
    }

    if (state.isError) {
        Toast.makeText(
            context,
            stringResource(R.string.fill_profile_screen_error),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.handleIntent(EditProfileIntent.ErrorShown)
    }
}


@Composable
private fun EditProfileScreenContent(
    state: EditProfileUiState,
    onNavigateClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onFullNameChanged: (String) -> Unit,
    onGenderChanged: (gender: Gender) -> Unit,
    onDateChanged: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDatePickerOpen by rememberSaveable {
        mutableStateOf(false)
    }

    LoadingDialog(open = state.isLoading)
    DatePickerDialog(
        open = isDatePickerOpen,
        onDismiss = { isDatePickerOpen = false },
        initialSelectedDate = state.dateOfBirth,
        onSelect = { newDate ->
            onDateChanged(newDate)
            isDatePickerOpen = false
        }
    )

    Scaffold(
        modifier = modifier,
        containerColor = HeliaTheme.backgroundColor,
        topBar = {
            Navbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                title = stringResource(R.string.edit_profile_screen_heading),
                onNavigateClick = onNavigateClick,
                actions = {}
            )
        },
        bottomBar = {
            EditProfileBottomBar(onClick = onUpdateClick)
        },
        content = { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(scaffoldPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                FullNameInput(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.fullName ?: "",
                    onValueChange = onFullNameChanged
                )
                Spacer(modifier = Modifier.height(24.dp))
                DateOfBirth(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.dateOfBirth.toUiString(),
                    onClick = { isDatePickerOpen = true }
                )
                Spacer(modifier = Modifier.height(24.dp))
                GenderInput(
                    modifier = Modifier.fillMaxWidth(),
                    gender = state.gender,
                    onValueChanged = onGenderChanged
                )
            }
        }
    )

}

@Composable
private fun FullNameInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    InputField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholderText = stringResource(R.string.full_name)
    )
}

@Composable
private fun DateOfBirth(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InputField(
        modifier = modifier
            .clip(InputFieldDefaults.shape)
            .clickable(onClick = onClick),
        value = value,
        enabled = false,
        onValueChange = {},
        placeholderText = stringResource(R.string.date_of_birth),
        trailingIcon = {
            Icon(
                modifier = Modifier.size(InputFieldDefaults.iconSize),
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = null
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderInput(
    gender: Gender?,
    onValueChanged: (gender: Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Column {
            InputField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clip(InputFieldDefaults.shape)
                    .clickable { expanded = true },
                value = gender?.let { stringResource(gender.nameStringResId) } ?: "",
                enabled = false,
                onValueChange = {},
                placeholderText = stringResource(R.string.gender),
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(InputFieldDefaults.iconSize),
                        painter = painterResource(R.drawable.ic_arrow_down_2),
                        contentDescription = null
                    )
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // provide a custom shape as the ExposedDropdownMenu uses a MaterialTheme extra small shape as a shape
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(extraSmall = HeliaTheme.shapes.medium as CornerBasedShape),
            colorScheme = MaterialTheme.colorScheme.copy(surface = HeliaTheme.backgroundColor)
        ) {

            ExposedDropdownMenu(
                modifier = Modifier,
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Gender.MALE.nameStringResId),
                            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
                        )
                    },
                    onClick = {
                        onValueChanged(Gender.MALE)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Gender.FEMALE.nameStringResId),
                            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
                        )
                    },
                    onClick = {
                        onValueChanged(Gender.FEMALE)
                        expanded = false
                    }
                )
            }
        }
    }

}

@Composable
private fun EditProfileBottomBar(onClick: () -> Unit) {
    PrimaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        text = stringResource(R.string.edit_profile_screen_update_button),
        onClick = onClick
    )
}

