package app.dmvtestprep.core.utils

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

object TimeUtils {
    private const val TIME_THRESHOLD = 24 * 60 * 60 * 1000L

    fun isUpToDate(timestamp: Long?, days: Long = 1): Boolean {
        return timestamp?.let {
            (currentTimestamp() - it) < TIME_THRESHOLD * days
        } ?: false
    }

    fun isUpToDate(currentTimestamp: Long, timestamp: Long?, days: Long = 1): Boolean {
        return timestamp?.let {
            (currentTimestamp - it) < TIME_THRESHOLD * days / 4
        } ?: false
    }

    @OptIn(ExperimentalTime::class)
    fun currentTimestamp(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    fun formatDuration(milliseconds: Long): String {
        return listOf(
            milliseconds / 86400000 to "d",
            milliseconds / 3600000 % 24 to "h",
            milliseconds / 60000 % 60 to "min",
            milliseconds / 1000 % 60 to "s"
        ).filter { it.first > 0 }
            .joinToString(" ") { "${it.first} ${it.second}" }
                .ifEmpty { "0 sec" }
    }

    @OptIn(ExperimentalTime::class)
    fun formatDateTime(timestamp: Long): String {
        return Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .run {
                listOf(
                    listOf(year, pad(month.number), pad(day)).joinToString("-"),
                    listOf(pad(hour), pad(minute), pad(second)).joinToString(":")
                ).joinToString(" ")
            }
    }

    private fun pad(value: Int): String {
        return value.toString().padStart(2, '0')
    }
}