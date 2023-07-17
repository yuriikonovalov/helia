package com.yuriikonovalov.helia.data.api.service

import com.squareup.moshi.JsonClass
import com.yuriikonovalov.helia.data.api.model.HotelProfile

@JsonClass(generateAdapter = true)
data class HotelsResponse(
    val data: List<HotelProfile>
)
