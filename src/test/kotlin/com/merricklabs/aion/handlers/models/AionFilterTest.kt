package com.merricklabs.aion.handlers.models

import biweekly.component.VEvent
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.external.LocationResult
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.params.LocationFilter
import com.merricklabs.aion.testutil.AionTestData.BEND_LAT
import com.merricklabs.aion.testutil.AionTestData.BEND_LONG
import com.merricklabs.aion.testutil.AionTestData.POWELLS_LAT
import com.merricklabs.aion.testutil.AionTestData.POWELLS_LONG
import com.merricklabs.aion.testutil.AionTestData.TEST_ADDRESS
import io.kotlintest.shouldBe
import org.koin.core.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.testng.annotations.Test

class AionFilterTest : AionIntegrationTestBase() {

    @Test
    fun `Apply a filter to an event's subject`() {
        val geocoderClient by inject<GeocoderClient>()
        val event = VEvent()
        event.setSummary("foo")
        val subjectFilters = FieldFilter(listOf("foo"), null)
        val filter = AionFilter(EntityId.create(), subjectFilters)
        val result = filter.apply(event, geocoderClient)
        result shouldBe true
    }

    @Test
    fun `Filter out unwanted subjects`() {
        val geocoderClient by inject<GeocoderClient>()
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(listOf("foo"), null)
        val filter = AionFilter(EntityId.create(), subjectFilters)
        val result = filter.apply(event, geocoderClient)
        result shouldBe false
    }

    @Test
    fun `Exclude event subject`() {
        val geocoderClient by inject<GeocoderClient>()
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(null, listOf("bar"))
        val filter = AionFilter(EntityId.create(), subjectFilters)
        val result = filter.apply(event, geocoderClient)
        result shouldBe false
    }

    @Test
    fun `Negative test for exclude filter`() {
        val geocoderClient by inject<GeocoderClient>()
        val event = VEvent()
        event.setSummary("bar")
        val subjectFilters = FieldFilter(null, listOf("foo"))
        val filter = AionFilter(EntityId.create(), subjectFilters)
        val result = filter.apply(event, geocoderClient)
        result shouldBe true
    }

    @Test
    fun `Filter event out by location`() {
        val event = VEvent()
        event.setLocation(TEST_ADDRESS)

        // Bend is more than 10 miles from Powell's Books
        val locationFilter = LocationFilter(BEND_LAT, BEND_LONG, 10)
        val filter = AionFilter(EntityId.create(), locationFilter = locationFilter)
        declareMock<GeocoderClient> {
            given(this.fetchLocation(any())).willReturn(LocationResult(POWELLS_LAT, POWELLS_LONG))
        }
        val geocoderClient by inject<GeocoderClient>()

        val result = filter.apply(event, geocoderClient)
        result shouldBe false
    }

    @Test
    fun `Negative test for filter event out by location`() {
        val geocoderClient by inject<GeocoderClient>()
        val event = VEvent()
        event.setLocation(TEST_ADDRESS)

        // Bend is less than 5000 km from Powell's Books
        val locationFilter = LocationFilter(BEND_LAT, BEND_LONG, 5000)
        val filter = AionFilter(EntityId.create(), locationFilter = locationFilter)

        val result = filter.apply(event, geocoderClient)
        result shouldBe true
    }
}