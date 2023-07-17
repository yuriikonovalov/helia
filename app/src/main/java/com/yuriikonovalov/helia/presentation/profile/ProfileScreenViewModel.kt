package com.yuriikonovalov.helia.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.GetAppPreferencesUseCase
import com.yuriikonovalov.helia.domain.usecases.GetUserPhotoUseCase
import com.yuriikonovalov.helia.domain.usecases.ObserveUserUseCase
import com.yuriikonovalov.helia.domain.usecases.SignOutUseCase
import com.yuriikonovalov.helia.domain.usecases.UpdateThemePreferenceUseCase
import com.yuriikonovalov.helia.domain.usecases.UpdateUserPhotoUseCase
import com.yuriikonovalov.helia.domain.valueobjects.Theme
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val getAppPreferences: GetAppPreferencesUseCase,
    private val updateThemePreference: UpdateThemePreferenceUseCase,
    private val signOut: SignOutUseCase,
    private val observeUser: ObserveUserUseCase,
    private val getUserPhoto: GetUserPhotoUseCase,
    private val updateUserPhoto: UpdateUserPhotoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileScreenUiState())
    val state = _state.asStateFlow()

    init {
        collectUserPhoto()
        collectDarkThemePreference()
        collectUserInfo()
    }

    fun handleIntent(intent: ProfileIntent) = when (intent) {
        ProfileIntent.Logout -> handleSignOut()
        is ProfileIntent.ToggleDarkTheme -> handleToggleDarkTheme(intent.checked)
        is ProfileIntent.UpdatePhoto -> handleUpdatePhoto(intent.uri)
    }

    private fun handleUpdatePhoto(uri: Uri?) {
        viewModelScope.launch {
            _state.update { it.updateIsPhotoUploading(true) }
            updateUserPhoto(uri)
            _state.update { it.updateIsPhotoUploading(false) }
        }
    }

    private fun handleToggleDarkTheme(checked: Boolean) {
        viewModelScope.launch {
            val theme = if (checked) Theme.DARK else Theme.LIGHT
            updateThemePreference(theme)
        }
    }

    private fun handleSignOut() {
        viewModelScope.launch {
            signOut()
        }
    }

    private fun collectUserInfo() {
        viewModelScope.launch {
            _state.update { it.updateIsUserLoading(true) }
            observeUser()
                .collect { result ->
                    _state.update { it.updateIsUserLoading(false) }
                    when (result) {
                        is Result.Success -> _state.update {
                            it.updateProfile(
                                name = result.data.fullName,
                                email = result.data.email ?: ""
                            )
                        }

                        is Result.Error -> {
                            result.exception?.printStackTrace()
                        }
                    }
                }


        }
    }

    private fun collectDarkThemePreference() {
        viewModelScope.launch {
            getAppPreferences()
                .map { it.theme }
                .map { it == Theme.DARK }
                .collectLatest { isDarkTheme ->
                    _state.update { it.updateIsDarkTheme(isDarkTheme) }
                }
        }
    }

    private fun collectUserPhoto() {
        viewModelScope.launch {
            getUserPhoto().collect { result ->
                when (result) {
                    is Result.Success -> _state.update { it.updatePhotoUri(result.data) }
                    is Result.Error -> {
                        result.exception?.printStackTrace()
                    }
                }
            }
        }
    }
}