package com.merricklabs.aion.storage.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.merricklabs.aion.models.AionFilter
import java.util.UUID

/**
 * The DynamoDB mapper expects a class with an empty
 * constructor and mutable params.
 * This class serves to confine that mutability to just the
 * storage layer.
 * See https://stackoverflow.com/questions/51073135/dynamodbmapper-load-cannot-instantiate-kotlin-data-class
 */
class DbAionFilter @JvmOverloads constructor(
        @DynamoDBHashKey var id: UUID? = null,
        @DynamoDBAttribute var titleFilters: List<String>? = null
)

fun AionFilter.toDb(): DbAionFilter {
    return DbAionFilter(id = this.id, titleFilters = this.titleFilters)
}

