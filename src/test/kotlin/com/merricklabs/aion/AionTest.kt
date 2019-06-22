package com.merricklabs.aion

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import org.testng.annotations.Test
import java.io.File


class AionTest {

    @Test
    private fun `Parse calendar`() {
        val calendarString = File(ClassLoader.getSystemResource("meetup.ics").file).readText()
        val calendar = Biweekly.parse(calendarString).first()

        val filtered = calendar.events.asSequence()
                .filter { !it.summary.value.toLowerCase().contains("microbiome") }
                .toList()

        val cloned = copyWithEvents(calendar, filtered)
        val output = Biweekly.write(cloned).go()
        println(output)
    }

    private fun copyWithEvents(original: ICalendar, events: List<VEvent>): ICalendar {
        val newCalendar = ICalendar(original)
        newCalendar.setComponent(VEvent::class.java, null)
        events.forEach { newCalendar.addEvent(it) }
        return newCalendar
    }
}