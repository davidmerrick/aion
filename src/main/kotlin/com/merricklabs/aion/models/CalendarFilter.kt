package com.merricklabs.aion.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import java.util.UUID

@DynamoDBTable(tableName = "aeon")
data class CalendarFilter(@DynamoDBHashKey val id: UUID,
                          @DynamoDBAttribute val url: String) {
    companion object {
        fun create(url: String) = CalendarFilter(UUID.randomUUID(), url)
    }
}