package com.merricklabs.aion.handlers

import org.koin.core.KoinComponent

class HelloModule : KoinComponent {
    fun sayHello() = "hello my darling"
}