package com.merricklabs.aion.handlers.logic

import com.merricklabs.aion.handlers.models.AionCalendar
import com.merricklabs.aion.handlers.models.CreateCalendarPayload
import com.merricklabs.aion.handlers.models.toDomain
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.storage.CalendarStorage
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class CalendarLogic : KoinComponent {

    private val storage by inject<CalendarStorage>()

    fun createCalendar(createPayload: CreateCalendarPayload): AionCalendar {
        val toCreate = createPayload.toDomain()
        storage.saveCalendar(toCreate)
        return storage.getCalendar(toCreate.id)
    }

    fun getCalendar(id: EntityId): AionCalendar {
        return storage.getCalendar(id)
    }
}