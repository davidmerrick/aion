package com.merricklabs.aion.storage.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.params.LocationFilter
import com.merricklabs.aion.params.RsvpStatus
import com.merricklabs.aion.resources.models.AionFilter
import com.merricklabs.aion.storage.util.EntityIdTypeConverter

/**
 * The DynamoDB mapper expects a class with an empty
 * constructor and mutable params.
 * This class serves to confine that mutability to just the
 * storage layer.
 * See https://stackoverflow.com/questions/51073135/dynamodbmapper-load-cannot-instantiate-kotlin-data-class
 */
class DbAionFilter @JvmOverloads constructor(
        @DynamoDBTypeConverted(converter = EntityIdTypeConverter::class)
        @DynamoDBHashKey var id: EntityId? = null,
        @DynamoDBAttribute var summaryFilter: FieldFilter? = null,
        @DynamoDBAttribute var locationFilter: LocationFilter? = null,
        @DynamoDBAttribute var rsvpStatuses: List<RsvpStatus>? = null,
        @DynamoDBAttribute var description: String? = null
)

fun AionFilter.toDb(): DbAionFilter {
    return DbAionFilter(
            id = id,
            summaryFilter = summaryFilter,
            locationFilter = locationFilter,
            rsvpStatuses = rsvpStatuses,
            description = description
    )
}

