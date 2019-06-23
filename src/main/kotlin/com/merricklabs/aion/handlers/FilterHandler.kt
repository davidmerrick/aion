package com.merricklabs.aion.handlers

import com.merricklabs.aion.handlers.logic.FilterLogic
import com.merricklabs.aion.handlers.util.AionLogic
import org.koin.core.inject

class FilterHandler : AionHandler() {
    override fun getLogic(): AionLogic {
        val logic by inject<FilterLogic>()
        return logic
    }
}