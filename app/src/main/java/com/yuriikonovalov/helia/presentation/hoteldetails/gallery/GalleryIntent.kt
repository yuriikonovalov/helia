package com.yuriikonovalov.helia.presentation.hoteldetails.gallery

sealed interface GalleryIntent {
    data class OpenImage(val url: String) : GalleryIntent
    data object CloseImage : GalleryIntent
}