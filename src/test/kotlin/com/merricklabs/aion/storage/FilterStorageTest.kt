package com.merricklabs.aion.storage

import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.resources.models.AionFilter
import io.kotlintest.shouldBe
import org.koin.test.inject
import org.testng.annotations.Test

class FilterStorageTest : AionIntegrationTestBase() {

    private val filterStorage by inject<FilterStorage>()

    @Test
    fun `Create filter`() {
        val subjectFilter = FieldFilter(listOf("foo"), listOf())
        val toCreate = AionFilter(EntityId.create(), subjectFilter)
        filterStorage.saveFilter(toCreate)
        val retrieved = filterStorage.getFilter(toCreate.id)
        retrieved.id shouldBe toCreate.id
    }
}