package com.merricklabs.aion.handlers

import com.merricklabs.aion.handlers.logic.ApplyFilterLogic
import com.merricklabs.aion.handlers.util.AionLogic
import org.koin.core.inject

class ApplyFilterHandler : AionHandler() {
    override fun getLogic(): AionLogic {
        val logic by inject<ApplyFilterLogic>()
        return logic
    }
}