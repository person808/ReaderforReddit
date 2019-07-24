package com.kainalu.readerforreddit.util

import android.content.Context
import com.kainalu.readerforreddit.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.temporal.ChronoUnit

fun LocalDateTime.getPostTime(context: Context, zoneOffset: ZoneOffset = ZoneOffset.UTC): String {
    val currentTime = LocalDateTime.now(zoneOffset)
    val years = ChronoUnit.YEARS.between(this, currentTime).toInt()
    val months = ChronoUnit.MONTHS.between(this, currentTime).toInt()
    val days = ChronoUnit.DAYS.between(this, currentTime).toInt()
    val hours = ChronoUnit.HOURS.between(this, currentTime).toInt()
    val minutes = ChronoUnit.MINUTES.between(this, currentTime).toInt()

    return when {
        years > 0 -> context.resources.getQuantityString(R.plurals.years_placeholder, years, years)
        months > 0 -> context.resources.getQuantityString(R.plurals.months_placeholder, months, months)
        days > 0 -> context.resources.getQuantityString(R.plurals.days_placeholder, days, days)
        hours > 0 -> context.resources.getQuantityString(R.plurals.hours_placeholder, hours, hours)
        minutes >= 1 -> context.resources.getQuantityString(R.plurals.minutes_placeholder, minutes, minutes)
        else -> context.getString(R.string.just_now)
    }
}

fun Int.getFormattedString(): String {
    return if (this < 1000) {
        this.toString()
    } else {
        "%.1fk".format(this / 1000.0)
    }
}
