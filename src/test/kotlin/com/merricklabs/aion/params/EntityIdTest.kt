package com.merricklabs.aion.params

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.testng.annotations.Test

class EntityIdTest {

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun `Test EntityId length`() {
        val invalid = "foo"
        EntityId(invalid)
    }

    @Test
    fun `ids should be unique`() {
        val id1 = EntityId.create()
        val id2 = EntityId.create()
        id1 shouldNotBe id2
    }

    @Test
    fun `ids should be set length`() {
        val id = EntityId.create()
        id.toString().length shouldBe ID_LENGTH
    }
}