package com.merricklabs.aion.params

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument

@DynamoDBDocument
data class PartStatFilter @JvmOverloads constructor(
        @DynamoDBAttribute var rsvpStatus: List<RsvpStatus>? = emptyList()
) {
    fun apply(fieldValue: RsvpStatus): Boolean {
        rsvpStatus?.let { statuses ->
            return statuses.contains(fieldValue)
        }

        return true
    }
}