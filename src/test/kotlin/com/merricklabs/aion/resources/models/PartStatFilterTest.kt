package com.merricklabs.aion.resources.models

import biweekly.Biweekly
import biweekly.ICalendar
import com.google.common.io.Resources
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.FACEBOOK_CAL_FILENAME
import com.merricklabs.aion.params.PartStatFilter
import com.merricklabs.aion.params.RsvpStatus.ACCEPTED
import com.merricklabs.aion.params.RsvpStatus.TENTATIVE
import com.merricklabs.aion.resources.util.getPartStat
import io.kotlintest.shouldBe
import org.testng.annotations.Test

class PartStatFilterTest : AionIntegrationTestBase() {

    @Test
    private fun `Filter events I'm going to`() {
        val calendar = getFacebookCalendar()
        val filter = PartStatFilter(listOf(ACCEPTED))
        val filtered = calendar.events.filter { event ->
            event.getPartStat()?.let {
                filter.apply(it)
            } ?: false
        }
        filtered.size shouldBe 8
    }

    @Test
    private fun `Filter events I'm tentative to`() {
        val calendar = getFacebookCalendar()
        val filter = PartStatFilter(listOf(TENTATIVE))
        val filtered = calendar.events.filter { event ->
            event.getPartStat()?.let {
                filter.apply(it)
            } ?: false
        }
        filtered.size shouldBe 9
    }

    @Test
    private fun `Filter events I'm tentative or going to`() {
        val calendar = getFacebookCalendar()
        val filter = PartStatFilter(listOf(TENTATIVE, ACCEPTED))
        val filtered = calendar.events.filter { event ->
            event.getPartStat()?.let {
                filter.apply(it)
            } ?: false
        }
        filtered.size shouldBe 17
    }

    private fun getFacebookCalendar(): ICalendar {
        val fileContent: String = Resources.getResource(FACEBOOK_CAL_FILENAME).readText()
        return Biweekly.parse(fileContent).first()
    }
}