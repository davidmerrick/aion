package com.merricklabs.aion.resources.models

import biweekly.Biweekly
import biweekly.ICalendar
import com.google.common.io.Resources
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.FACEBOOK_CAL_FILENAME
import com.merricklabs.aion.params.PartStatFilter
import com.merricklabs.aion.params.RsvpStatus
import io.kotlintest.shouldBe
import org.testng.annotations.Test

class PartStatFilterTest : AionIntegrationTestBase() {

    @Test
    private fun `Filter events I'm going to`() {
        val calendar = getFacebookCalendar()
        val filter = PartStatFilter(listOf(RsvpStatus.ACCEPTED))
        val filtered = calendar.events.filter { event ->
            val statusString = event.getExperimentalProperty("PARTSTAT").value
            RsvpStatus.from(statusString)?.let {
                filter.apply(it)
            } ?: false
        }
        filtered.size shouldBe 8
    }

    private fun getFacebookCalendar(): ICalendar {
        val fileContent: String = Resources.getResource(FACEBOOK_CAL_FILENAME).readText()
        return Biweekly.parse(fileContent).first()
    }
}