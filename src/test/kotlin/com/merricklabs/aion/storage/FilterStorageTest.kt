package com.merricklabs.aion.storage

import biweekly.Biweekly
import com.google.common.io.Resources
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.FACEBOOK_CAL_FILENAME
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.params.PartStatFilter
import com.merricklabs.aion.params.RsvpStatus
import com.merricklabs.aion.resources.models.AionFilter
import io.kotlintest.shouldBe
import org.koin.test.inject
import org.testng.annotations.Test

class FilterStorageTest : AionIntegrationTestBase() {

    private val filterStorage by inject<FilterStorage>()
    private val geocoderClient by inject<GeocoderClient>()

    @Test
    fun `Create filter`() {
        val subjectFilter = FieldFilter(listOf("foo"), listOf())
        val toCreate = AionFilter(EntityId.create(), subjectFilter)
        filterStorage.saveFilter(toCreate)
        val retrieved = filterStorage.getFilter(toCreate.id)
        retrieved.id shouldBe toCreate.id
    }

    @Test
    fun `Filter by partstat`() {
        val partStatFilter = PartStatFilter(listOf(RsvpStatus.ACCEPTED))
        val toCreate = AionFilter(EntityId.create(), partStatFilter = partStatFilter)
        filterStorage.saveFilter(toCreate)
        val created = filterStorage.getFilter(toCreate.id)

        val fileContent: String = Resources.getResource(FACEBOOK_CAL_FILENAME).readText()
        val calendar = Biweekly.parse(fileContent).first()
        val filtered = calendar.events.filter {
            created.apply(it, geocoderClient)
        }
        filtered.size shouldBe 8
    }
}