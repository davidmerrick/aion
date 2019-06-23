package com.merricklabs.aion.handlers

import com.merricklabs.aion.handlers.logic.FilterLogic

class FilterHandler : AionHandler() {
    override fun getLogic() = FilterLogic()
}