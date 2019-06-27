package com.merricklabs.aion.handlers.util

import biweekly.ICalendar
import biweekly.component.VEvent
import com.merricklabs.aion.handlers.models.AionFilter

fun ICalendar.applyFilter(filter: AionFilter): ICalendar {
    val filtered = this.events.filter { filter.applyWithoutLocation(it) }
    return this.copyWithEvents(filtered)
}

fun ICalendar.copyWithEvents(events: List<VEvent>): ICalendar {
    val newCalendar = ICalendar(this)
    newCalendar.setComponent(VEvent::class.java, null)
    events.forEach { newCalendar.addEvent(it) }
    return newCalendar
}
