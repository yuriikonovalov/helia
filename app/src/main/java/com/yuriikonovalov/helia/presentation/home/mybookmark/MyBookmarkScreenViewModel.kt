package com.yuriikonovalov.helia.presentation.home.mybookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuriikonovalov.helia.domain.usecases.BookmarkHotelUseCase
import com.yuriikonovalov.helia.domain.usecases.GetBookmarkedHotelsUseCase
import com.yuriikonovalov.helia.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBookmarkScreenViewModel @Inject constructor(
    private val bookmarkHotel: BookmarkHotelUseCase,
    private val getBookmarkedHotels: GetBookmarkedHotelsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MyBookmarkUiState())
    val state = _state.asStateFlow()

    init {
        collectBookmarkedHotels()
    }


    fun handleIntent(intent: MyBookmarkIntent) = when (intent) {
        is MyBookmarkIntent.DeleteBookmark -> handleDeleteBookmark(intent.hotelId)
        is MyBookmarkIntent.ChangeViewMode -> _state.update { it.updateDisplayMode(intent.hotelDisplayMode) }
        is MyBookmarkIntent.OpenBottomSheet -> _state.update { it.updateHotelIdToDelete(intent.hotelId) }
        MyBookmarkIntent.HideBottomSheet -> _state.update { it.updateHotelIdToDelete(null) }
    }


    private fun collectBookmarkedHotels() {
        viewModelScope.launch {
            _state.update { it.updateIsLoading(true) }
            getBookmarkedHotels()
                .collect { result ->
                    _state.update { it.updateIsLoading(false) }
                    when (result) {
                        is Result.Error -> _state.update { it.updateHotels(emptyList()) }
                        is Result.Success -> _state.update { it.updateHotels(result.data) }
                    }
                }
        }
    }

    private fun handleDeleteBookmark(hotelId: String) {
        viewModelScope.launch {
            _state.update { it.updateHotelIdToDelete(null) }
            _state.update { it.updateIsDeleting(true) }

            bookmarkHotel(hotelId = hotelId, bookmark = false)

            _state.update { it.updateIsDeleting(false) }
        }
    }
}