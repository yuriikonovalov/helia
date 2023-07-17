package com.yuriikonovalov.helia.presentation.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.GetUserUseCase
import com.yuriikonovalov.helia.domain.usecases.UpdateProfileUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileScreenViewModel @Inject constructor(
    private val getUser: GetUserUseCase,
    private val updateProfile: UpdateProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = getUser()
            _state.update { it.updateIsLoading(false) }
            when (result) {
                is Result.Success -> _state.update {
                    it.updateUser(
                        result.data.fullName,
                        result.data.dateOfBirth,
                        result.data.gender
                    )
                }

                is Result.Error -> {}
            }
        }
    }

    fun handleIntent(intent: EditProfileIntent) = when (intent) {
        EditProfileIntent.Update -> handleUpdate()
        EditProfileIntent.ErrorShown -> _state.update { it.updateIsError(false) }
        is EditProfileIntent.InputFullName -> _state.update { it.updateFullName(intent.name) }
        is EditProfileIntent.UpdateDateOfBirth -> _state.update { it.updateDateOfBirth(intent.date) }
        is EditProfileIntent.UpdateGender -> _state.update { it.updateGender(intent.gender) }
    }

    private fun handleUpdate() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = updateProfile(
                fullName = state.value.fullName,
                dateOfBirth = state.value.dateOfBirth,
                gender = state.value.gender,
                updatePhotoUri = false
            )
            _state.update { it.updateIsLoading(false) }
            when (result) {
                is Result.Success -> _state.update { it.updateIsProfileUpdated(true) }
                is Result.Error -> _state.update { it.updateIsError(true) }
            }
        }

    }
}