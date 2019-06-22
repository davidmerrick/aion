package com.merricklabs.aion.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import java.util.UUID

data class CalendarFilter(@DynamoDBHashKey val id: UUID,
                          @DynamoDBAttribute val url: String) {
    companion object {
        fun create(url: String) = CalendarFilter(UUID.randomUUID(), url)
    }
}