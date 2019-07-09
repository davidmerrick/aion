package com.merricklabs.aion.params

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped

@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
enum class RsvpStatus(@DynamoDBAttribute var value: String) {
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED"),
    TENTATIVE("TENTATIVE"),
    NEEDS_ACTION("NEEDS-ACTION");

    companion object {
        private val statusMap = RsvpStatus
                .values()
                .associateBy(RsvpStatus::value)

        fun from(value: String) = statusMap[value]
    }
}