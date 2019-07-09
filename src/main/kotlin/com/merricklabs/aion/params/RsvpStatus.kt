package com.merricklabs.aion.params

enum class RsvpStatus(val value: String) {
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