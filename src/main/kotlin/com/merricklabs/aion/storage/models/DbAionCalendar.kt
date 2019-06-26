package com.merricklabs.aion.storage.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.merricklabs.aion.handlers.models.AionCalendar

/**
 * The DynamoDB mapper expects a class with an empty
 * constructor and mutable params.
 * This class serves to confine that mutability to just the
 * storage layer.
 * See https://stackoverflow.com/questions/51073135/dynamodbmapper-load-cannot-instantiate-kotlin-data-class
 */
class DbAionCalendar @JvmOverloads constructor(
        @DynamoDBHashKey var id: String? = null,
        @DynamoDBAttribute var url: String? = null
)

fun AionCalendar.toDb(): DbAionCalendar {
    return DbAionCalendar(id = id.value, url = url)
}

