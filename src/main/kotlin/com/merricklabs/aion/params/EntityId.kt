package com.merricklabs.aion.params

import com.merricklabs.aion.hashids.Hashids
import java.io.Serializable
import java.util.UUID

const val ID_LENGTH = 11

data class EntityId(val value: String): Serializable {
    init {
        require(value.length == ID_LENGTH) {
            "Entity id length must be $ID_LENGTH chars"
        }
    }

    override fun toString() = value

    companion object {
        fun create(): EntityId {
            val hashids = Hashids(UUID.randomUUID().toString(), ID_LENGTH)
            return EntityId(hashids.encode(System.currentTimeMillis() / 1000L))
        }
    }
}