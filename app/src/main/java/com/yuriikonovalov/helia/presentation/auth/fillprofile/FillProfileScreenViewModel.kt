package com.yuriikonovalov.helia.presentation.auth.fillprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.UpdateProfileUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FillProfileScreenViewModel @Inject constructor(
    private val updateProfile: UpdateProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FillProfileUiState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: FillProfileIntent) = when (intent) {
        is FillProfileIntent.UpdatePhoto -> _state.update { it.updatePhotoUri(intent.uri) }
        is FillProfileIntent.InputFullName -> _state.update { it.inputFullName(intent.fullName) }
        is FillProfileIntent.SelectDateOfBirth -> _state.update { it.selectDateOfBirth(intent.dateOfBirth) }
        is FillProfileIntent.SelectGender -> _state.update { it.selectGender(intent.gender) }
        FillProfileIntent.ClickDateOfBirth -> _state.update { it.updateIsDatePickerOpen(true) }
        FillProfileIntent.DismissDatePicker -> _state.update { it.updateIsDatePickerOpen(false) }
        FillProfileIntent.ErrorShown -> _state.update { it.updateIsError(false) }
        FillProfileIntent.Continue -> handleContinue()
    }

    private fun handleContinue() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = updateProfile(
                fullName = state.value.fullName.trim(),
                dateOfBirth = state.value.dateOfBirth!!,
                gender = state.value.gender!!,
                photoUri = state.value.photoUri
            )
            _state.update { it.updateIsLoading(false) }
            when (result) {
                is Result.Success -> _state.update { it.profileUpdated() }
                is Result.Error -> {
                    Log.d("FillProfileScreenViewModel", result.exception?.toString().toString())
                    _state.update { it.updateIsError(true) }
                }
            }
        }
    }
}