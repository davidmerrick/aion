package com.merricklabs.aion.util

import com.merricklabs.aion.params.EntityId
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.testng.annotations.Test

const val ID_LENGTH = 11

class IdUtilTest {

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