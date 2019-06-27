package com.merricklabs.aion.params

import biweekly.component.VEvent
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument

@DynamoDBDocument
data class FieldFilter @JvmOverloads constructor(
        @DynamoDBAttribute var include: List<String>? = emptyList(),
        @DynamoDBAttribute var exclude: List<String>? = emptyList()
) {
    fun apply(event: VEvent): Boolean {
        include?.let { filters ->
            filters.asSequence().forEach {
                if (!event.summary.value.contains(it)) {
                    return false
                }
            }
        }

        exclude?.let { filters ->
            filters.asSequence().forEach {
                if (event.summary.value.contains(it)) {
                    return false
                }
            }
        }

        return true
    }
}