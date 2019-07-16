package com.merricklabs.aion.resources.models

import biweekly.Biweekly
import biweekly.ICalendar
import com.google.common.io.Resources
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.FACEBOOK_CAL_FILENAME
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.RsvpStatus.ACCEPTED
import com.merricklabs.aion.params.RsvpStatus.TENTATIVE
import io.kotlintest.shouldBe
import org.mockito.Mockito
import org.testng.annotations.Test

class RsvpFilterTest : AionIntegrationTestBase() {

    @Test
    private fun `Filter events I'm going to`() {
        val calendar = getFacebookCalendar()
        val filter = AionFilter(id = EntityId.create(), rsvpStatuses = listOf(ACCEPTED))
        val filtered = calendar.events.filter {
            filter.apply(it, Mockito.mock(GeocoderClient::class.java))
        }
        filtered.size shouldBe 8
    }

    @Test
    private fun `Filter events I'm tentative to`() {
        val calendar = getFacebookCalendar()
        val filter = AionFilter(id = EntityId.create(), rsvpStatuses = listOf(TENTATIVE))
        val filtered = calendar.events.filter {
            filter.apply(it, Mockito.mock(GeocoderClient::class.java))
        }
        filtered.size shouldBe 9
    }

    @Test
    private fun `Filter events I'm tentative or going to`() {
        val calendar = getFacebookCalendar()
        val filter = AionFilter(id = EntityId.create(), rsvpStatuses = listOf(TENTATIVE, ACCEPTED))
        val filtered = calendar.events.filter {
            filter.apply(it, Mockito.mock(GeocoderClient::class.java))
        }
        filtered.size shouldBe 17
    }

    private fun getFacebookCalendar(): ICalendar {
        val fileContent: String = Resources.getResource(FACEBOOK_CAL_FILENAME).readText()
        return Biweekly.parse(fileContent).first()
    }
}