package com.merricklabs.aion.util

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.testng.annotations.Test

class IdUtilTest {

    @Test
    fun `ids should be unique`(){
        val id1 = IdUtil.generateId()
        val id2 = IdUtil.generateId()
        id1 shouldNotBe id2
    }

    @Test
    fun `ids should be set length`(){
        val id = IdUtil.generateId()
        id.length shouldBe ID_LENGTH
    }

    private companion object {
        const val ID_LENGTH = 11
    }
}