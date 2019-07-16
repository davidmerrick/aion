package com.merricklabs.aion.resources.util

import biweekly.ICalendar
import biweekly.component.VEvent
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.params.RsvpStatus
import com.merricklabs.aion.resources.models.AionFilter

fun ICalendar.applyFilter(filter: AionFilter, geocoderClient: GeocoderClient): ICalendar {
    val filtered = this.events.filter { filter.apply(it, geocoderClient) }
    return this.copyWithEvents(filtered)
}

fun ICalendar.copyWithEvents(events: List<VEvent>): ICalendar {
    val newCalendar = ICalendar(this)
    newCalendar.setComponent(VEvent::class.java, null)
    events.forEach { newCalendar.addEvent(it) }
    return newCalendar
}

fun VEvent.getRsvpStatus() = this.getExperimentalProperty("PARTSTAT").let {
    RsvpStatus.from(it.value)
}
