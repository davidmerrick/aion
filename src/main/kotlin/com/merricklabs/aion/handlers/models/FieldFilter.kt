package com.merricklabs.aion.handlers.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute

data class FieldFilter(
        @DynamoDBAttribute var include: List<String>?,
        @DynamoDBAttribute var exclude: List<String>?
)