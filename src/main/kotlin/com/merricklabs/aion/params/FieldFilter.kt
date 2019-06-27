package com.merricklabs.aion.params

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument

@DynamoDBDocument
data class FieldFilter @JvmOverloads constructor(
        @DynamoDBAttribute var include: List<String>? = emptyList(),
        @DynamoDBAttribute var exclude: List<String>? = emptyList()
)