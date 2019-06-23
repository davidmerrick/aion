package com.merricklabs.aion.handlers.models

import biweekly.component.VEvent
import io.kotlintest.shouldBe
import org.testng.annotations.Test
import java.util.UUID

class AionFilterTest {

    @Test
    fun `Apply a filter to an event's subject`() {
        val event = VEvent()
        event.setSummary("foo")
        val subjectFilters = FieldFilter(listOf("foo"), null)
        val filter = AionFilter(UUID.randomUUID(), subjectFilters)
        val result = filter.apply(event)
        result shouldBe true
    }

    @Test
    fun `Filter out unwanted subjects`() {
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(listOf("foo"), null)
        val filter = AionFilter(UUID.randomUUID(), subjectFilters)
        val result = filter.apply(event)
        result shouldBe false
    }

    @Test
    fun `Exclude event subject`() {
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(null, listOf("bar"))
        val filter = AionFilter(UUID.randomUUID(), subjectFilters)
        val result = filter.apply(event)
        result shouldBe false
    }

    @Test
    fun `Negative test for exclude filter`() {
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(null, listOf("foo"))
        val filter = AionFilter(UUID.randomUUID(), subjectFilters)
        val result = filter.apply(event)
        result shouldBe true
    }
}