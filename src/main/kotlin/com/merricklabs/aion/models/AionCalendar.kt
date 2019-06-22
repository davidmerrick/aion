package com.merricklabs.aion.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import java.util.UUID

/**
 * Consists of a url for a calendar and filters for it.
 */
data class AionCalendar(@DynamoDBHashKey val id: UUID,
                        @DynamoDBAttribute val url: String) {
    companion object {
        fun create(url: String) = AionCalendar(UUID.randomUUID(), url)
    }
}