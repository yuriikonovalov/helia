package com.yuriikonovalov.helia.presentation.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDate.toMillis(): Long {
    return this.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.of("UTC")).toLocalDate()
}


fun LocalDate?.toUiString(): String =
    this?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) ?: ""

