package com.yuriikonovalov.helia.domain.entities

data class Hotel(
    val id: String,
    val imageUrl: String,
    val name: String,
    val country: String,
    val city: String,
    val rating: Double,
    val numberOfReviews: Int,
    val price: Double
)
