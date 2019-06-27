package com.merricklabs.aion.storage.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted
import com.merricklabs.aion.handlers.models.AionCalendar
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.storage.util.EntityIdTypeConverter

/**
 * The DynamoDB mapper expects a class with an empty
 * constructor and mutable params.
 * This class serves to confine that mutability to just the
 * storage layer.
 * See https://stackoverflow.com/questions/51073135/dynamodbmapper-load-cannot-instantiate-kotlin-data-class
 */
class DbAionCalendar @JvmOverloads constructor(
        @DynamoDBTypeConverted(converter = EntityIdTypeConverter::class)
        @DynamoDBHashKey var id: EntityId? = null,
        @DynamoDBAttribute var url: String? = null
)

fun AionCalendar.toDb(): DbAionCalendar {
    return DbAionCalendar(id = id, url = url)
}

