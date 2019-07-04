package com.merricklabs.aion.storage

import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.resources.models.AionCalendar
import com.merricklabs.aion.testutil.AionTestData
import io.kotlintest.shouldBe
import org.koin.test.inject
import org.testng.annotations.Test

class CalendarStorageTest : AionIntegrationTestBase() {

    private val calendarStorage by inject<CalendarStorage>()

    @Test
    fun `Create calendar`() {
        val toCreate = AionCalendar.create(AionTestData.TEST_URL)
        calendarStorage.saveCalendar(toCreate)
        val retrieved = calendarStorage.getCalendar(toCreate.id)
        retrieved.id shouldBe toCreate.id
    }
}