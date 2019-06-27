package com.merricklabs.aion.handlers.models

import biweekly.component.VEvent
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import io.kotlintest.shouldBe
import org.testng.annotations.Test

class AionFilterTest {

    @Test
    fun `Apply a filter to an event's subject`() {
        val event = VEvent()
        event.setSummary("foo")
        val subjectFilters = FieldFilter(listOf("foo"), null)
        val filter = AionFilter(EntityId.create(), subjectFilters)
        val result = filter.apply(event)
        result shouldBe true
    }

    @Test
    fun `Filter out unwanted subjects`() {
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(listOf("foo"), null)
        val filter = AionFilter(EntityId.create(), subjectFilters)
        val result = filter.apply(event)
        result shouldBe false
    }

    @Test
    fun `Exclude event subject`() {
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(null, listOf("bar"))
        val filter = AionFilter(EntityId.create(), subjectFilters)
        val result = filter.apply(event)
        result shouldBe false
    }

    @Test
    fun `Negative test for exclude filter`() {
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(null, listOf("foo"))
        val filter = AionFilter(EntityId.create(), subjectFilters)
        val result = filter.apply(event)
        result shouldBe true
    }
}