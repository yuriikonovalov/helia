package com.yuriikonovalov.helia.presentation.auth.fillprofile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.yuriikonovalov.helia.designsystem.components.Avatar
import com.yuriikonovalov.helia.designsystem.components.AvatarType
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
fun FillProfileScreen(
    onNavigateClick: () -> Unit,
    viewModel: FillProfileScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val photoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                viewModel.handleIntent(FillProfileIntent.UpdatePhoto(uri))
            }
        }

    SignUpFillProfileContent(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        state = state,
        onNavigateClick = onNavigateClick,
        onEditPhotoClick = {
            photoPickerLauncher.launch(PickVisualMediaRequest((ActivityResultContracts.PickVisualMedia.ImageOnly)))
        },
        onDeletePhoto = { viewModel.handleIntent(FillProfileIntent.UpdatePhoto(null)) },
        onContinueClick = { viewModel.handleIntent(FillProfileIntent.Continue) },
        onFullNameChanged = { viewModel.handleIntent(FillProfileIntent.InputFullName(it)) },
        onDateChanged = { viewModel.handleIntent(FillProfileIntent.SelectDateOfBirth(it)) },
        onGenderChanged = { viewModel.handleIntent(FillProfileIntent.SelectGender(it)) },
        onDateOfBirthClick = { viewModel.handleIntent(FillProfileIntent.ClickDateOfBirth) },
        onDatePickerDismiss = { viewModel.handleIntent(FillProfileIntent.DismissDatePicker) },
    )

    if (state.profileUpdated) {
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
        viewModel.handleIntent(FillProfileIntent.ErrorShown)
    }
}


@Composable
private fun SignUpFillProfileContent(
    state: FillProfileUiState,
    onNavigateClick: () -> Unit,
    onEditPhotoClick: () -> Unit,
    onDeletePhoto: () -> Unit,
    onContinueClick: () -> Unit,
    onFullNameChanged: (String) -> Unit,
    onDateOfBirthClick: () -> Unit,
    onGenderChanged: (gender: Gender) -> Unit,
    onDateChanged: (date: LocalDate) -> Unit,
    onDatePickerDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingDialog(open = state.isLoading)
    DatePickerDialog(
        open = state.isDatePickerOpen,
        onDismiss = onDatePickerDismiss,
        initialSelectedDate = state.dateOfBirth,
        onSelect = onDateChanged
    )

    Scaffold(
        modifier = modifier,
        containerColor = HeliaTheme.backgroundColor,
        topBar = {
            Navbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                title = stringResource(R.string.fill_your_profile_screen_heading),
                onNavigateClick = onNavigateClick,
                actions = {}
            )
        },
        bottomBar = {
            SignUnFillProfileBottomBar(
                enabled = state.isContinueButtonEnabled,
                onClick = onContinueClick
            )
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
                Avatar(
                    modifier = Modifier.size(100.dp),
                    uri = state.photoUri,
                    onEditClick = onEditPhotoClick,
                    onDeletePhoto = onDeletePhoto,
                    type = AvatarType.EDIT
                )
                Spacer(modifier = Modifier.height(24.dp))
                FullNameInput(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.fullName,
                    onValueChange = onFullNameChanged
                )
                Spacer(modifier = Modifier.height(24.dp))
                DateOfBirth(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.dateOfBirth.toUiString(),
                    onClick = onDateOfBirthClick
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
private fun SignUnFillProfileBottomBar(
    enabled: Boolean,
    onClick: () -> Unit
) {
    PrimaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        text = stringResource(R.string.edit_profile_screen_update_button),
        enabled = enabled,
        onClick = onClick
    )
}

