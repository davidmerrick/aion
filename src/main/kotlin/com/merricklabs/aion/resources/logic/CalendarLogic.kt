package com.merricklabs.aion.resources.logic

import biweekly.Biweekly
import com.merricklabs.aion.external.CalendarClient
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.resources.models.AionCalendar
import com.merricklabs.aion.resources.models.CreateCalendarPayload
import com.merricklabs.aion.resources.models.toDomain
import com.merricklabs.aion.resources.util.applyFilter
import com.merricklabs.aion.storage.CalendarStorage
import com.merricklabs.aion.storage.FilterStorage
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class CalendarLogic : KoinComponent {

    private val calendarStorage by inject<CalendarStorage>()
    private val filterStorage by inject<FilterStorage>()
    private val calendarClient by inject<CalendarClient>()
    private val geocoderClient by inject<GeocoderClient>()

    fun createCalendar(createPayload: CreateCalendarPayload): AionCalendar {
        calendarClient.validateCalendar(createPayload.url)

        val toCreate = createPayload.toDomain()
        calendarStorage.saveCalendar(toCreate)
        return calendarStorage.getCalendar(toCreate.id)
    }

    fun getCalendar(id: EntityId) = calendarStorage.getCalendar(id)

    fun getFilteredCalendar(calendarId: EntityId, filterId: EntityId): String {
        val calendarUrl = calendarStorage.getCalendar(calendarId).sanitizedUrl()
        val filter = filterStorage.getFilter(filterId)

        val filteredCalendar = calendarClient.fetchCalendar(calendarUrl).applyFilter(filter, geocoderClient)
        return Biweekly.write(filteredCalendar).go()
    }
}