package com.merricklabs.aion.handlers.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument

@DynamoDBDocument
data class FieldFilter(
        @DynamoDBAttribute var include: List<String>?,
        @DynamoDBAttribute var exclude: List<String>?
)