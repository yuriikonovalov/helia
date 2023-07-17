package com.yuriikonovalov.helia.presentation.hoteldetails.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.GetHotelImagesUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHotelImages: GetHotelImagesUseCase
) : ViewModel() {
    private val hotelId: String = checkNotNull(savedStateHandle[GalleryNavigation.ARGUMENT])
    private val _state = MutableStateFlow(GalleryUiState())
    val state = _state.asStateFlow()

    init {
        getImageUrls()
    }

    fun handleIntent(intent: GalleryIntent) = when (intent) {
        is GalleryIntent.OpenImage -> _state.update { it.updateOpenImageUrl(intent.url) }
        GalleryIntent.CloseImage -> _state.update { it.updateOpenImageUrl(null) }
    }

    private fun getImageUrls() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            val result = getHotelImages(hotelId)
            _state.update { it.updateIsLoading(false) }
            when (result) {
                is Result.Error -> TODO()
                is Result.Success -> _state.update { it.updateImageUrls(result.data) }
            }
        }
    }
}