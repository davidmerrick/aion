package com.merricklabs.aion.handlers

import com.merricklabs.aion.handlers.logic.CalendarLogic
import com.merricklabs.aion.handlers.util.AionLogic
import org.koin.core.inject

class CalendarHandler : AionHandler() {
    override fun getLogic(): AionLogic {
        val logic by inject<CalendarLogic>()
        return logic
    }
}