package com.merricklabs.aion.handlers

import com.merricklabs.aion.handlers.logic.ApplyFilterLogic

class ApplyFilterHandler : AionHandler() {
    override fun getLogic() = ApplyFilterLogic()
}