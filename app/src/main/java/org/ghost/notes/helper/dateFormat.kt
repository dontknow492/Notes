package org.ghost.notes.helper

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatEpochMilliToString(epochMilli: Long): String {
    // 1. Convert epoch milliseconds to an Instant
    val instant = Instant.ofEpochMilli(epochMilli)

    // 2. Convert the Instant to a LocalDateTime in the system's default time zone
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    // 3. Define the desired date format
    val formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")

    // 4. Format the LocalDateTime into a String
    return localDateTime.format(formatter)
}


fun formatEpochMilliToAgo(epochMilli: Long): String {
    // The current time in milliseconds
    val nowInMillis = System.currentTimeMillis()

    // DateUtils works with milliseconds, so we convert the input seconds

    val timeAgo = DateUtils.getRelativeTimeSpanString(
        epochMilli,
        nowInMillis,
        DateUtils.SECOND_IN_MILLIS // The minimum resolution to show (e.g., "5 seconds ago")
    ).toString()

    return timeAgo
}