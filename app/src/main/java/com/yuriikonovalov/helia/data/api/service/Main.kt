package com.yuriikonovalov.helia.data.api.service

import com.squareup.moshi.Moshi
import com.yuriikonovalov.helia.data.api.model.HotelProfile
import java.io.File
import java.io.FileOutputStream

fun main() {
    val hotels = randomHotels(100)

    generateHotels(hotels)
    generateCountries()
}

private fun generateCountries() {
    val moshi = Moshi.Builder().build()

    val adapter = moshi.adapter(CountriesResponse::class.java)

    val response = CountriesResponse(countries)
    val jsonString = adapter.toJson(response)

    val file = File("countries.json")

    FileOutputStream(file).use { outputStream ->
        outputStream.writer().use { writer ->
            writer.write(jsonString)
        }
    }
}

private fun generateHotels(hotels: List<HotelProfile>) {
    val moshi = Moshi.Builder()
        .add(LocalDateAdapter())
        .build()

    val adapter = moshi.adapter(HotelsResponse::class.java)
    val response = HotelsResponse(hotels)

    val jsonString = adapter.toJson(response)

    val file = File("hotels.json")

    FileOutputStream(file).use { outputStream ->
        outputStream.writer().use { writer ->
            writer.write(jsonString)
        }
    }
}
