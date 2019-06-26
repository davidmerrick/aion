package com.merricklabs.aion.util

import com.merricklabs.aion.hashids.Hashids
import java.util.UUID

object IdUtil {
    private const val HASH_LENGTH = 11

    fun generateId(): String {
        val hashids = Hashids(UUID.randomUUID().toString(), HASH_LENGTH)
        return hashids.encode(System.currentTimeMillis()/1000L)
    }
}