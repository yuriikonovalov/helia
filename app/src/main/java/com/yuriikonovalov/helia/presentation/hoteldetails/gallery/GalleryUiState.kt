package com.yuriikonovalov.helia.presentation.hoteldetails.gallery

data class GalleryUiState(
    val imageUrls: List<String> = emptyList(),
    val openImageUrl: String? = null,
    val isLoading: Boolean = false
) {
    fun updateImageUrls(value: List<String>) = copy(imageUrls = value)

    fun updateOpenImageUrl(value: String?) = copy(openImageUrl = value)

    fun updateIsLoading(value: Boolean) = copy(isLoading = value)
}
