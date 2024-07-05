package com.example.timestampapi

import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/api")
class DateController {

    companion object {
        private val gmtTimeZone: TimeZone = TimeZone.getTimeZone("GMT")
        private val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH).apply {
            timeZone = gmtTimeZone
        }
        private val dateFormatInput = SimpleDateFormat("yyyy-MM-dd").apply {
            timeZone = gmtTimeZone
        }

        private fun formatToGMT(date: Date): String = dateFormat.format(date)
        private fun parseDate(date: String): Date = date.toLongOrNull()?.let { Date(it) } ?: dateFormatInput.parse(date)
    }

    @GetMapping("/{date}")
    fun getDate(@PathVariable date: String): Map<String, Any> {
        return try {
            val parsedDate = parseDate(date)
            mapOf(
                "unix" to parsedDate.time,
                "utc" to formatToGMT(parsedDate)
            )
        } catch (e: Exception) {
            mapOf("error" to "Invalid Date")
        }
    }

    @GetMapping("/")
    fun getCurrentDate(): Map<String, Any> {
        val currentDate = Date()
        return mapOf(
            "unix" to currentDate.time,
            "utc" to formatToGMT(currentDate)
        )
    }
}
