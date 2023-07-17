package com.yuriikonovalov.helia.data.api.service

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @ToJson
    fun toJson(date: LocalDate): String {
        return date.format(formatter)
    }

    @FromJson
    fun fromJson(date: String): LocalDate {
        return formatter.parse(date, LocalDate::from)
    }
}