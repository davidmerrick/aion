package com.merricklabs.aion.exceptions

import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException

class InvalidBodyException : HttpResponseException {
    constructor() : super(HttpStatus.SC_BAD_REQUEST, "Invalid payload.")
}