package com.merricklabs.aion.external

import biweekly.Biweekly
import biweekly.ICalendar
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.KoinComponent

class CalendarClient : KoinComponent {

    private val okHttpClient = OkHttpClient()

    fun fetchCalendar(url: String): ICalendar {
        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()

        response.body()?.let {
            return Biweekly.parse(it.string()).first()
        }

        throw IllegalArgumentException("Calendar url returned no content.")
    }
}