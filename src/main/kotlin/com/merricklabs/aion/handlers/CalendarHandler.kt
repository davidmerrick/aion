package com.merricklabs.aion.handlers

import com.merricklabs.aion.handlers.logic.CalendarLogic

class CalendarHandler : AionHandler() {
    override fun getLogic() = CalendarLogic()
}