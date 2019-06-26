package com.merricklabs.aion.params

import com.merricklabs.aion.hashids.Hashids
import java.util.UUID

data class EntityId(val value: String) {
    init {
        require(value.length == ID_LENGTH) {
            "Entity id length must be $ID_LENGTH chars"
        }
    }

    companion object {
        const val ID_LENGTH = 11

        fun create(): EntityId {
            val hashids = Hashids(UUID.randomUUID().toString(), ID_LENGTH)
            return EntityId(hashids.encode(System.currentTimeMillis() / 1000L))
        }
    }
}