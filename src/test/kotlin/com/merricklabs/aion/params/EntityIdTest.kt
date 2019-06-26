package com.merricklabs.aion.params

import org.testng.annotations.Test
import java.lang.IllegalArgumentException

class EntityIdTest {

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun `Test EntityId length`(){
        val invalid = "foo"
        EntityId(invalid)
    }
}