package com.merricklabs.aion.exceptions

import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException

class InvalidCalendarException : HttpResponseException {
    constructor() : super(HttpStatus.SC_BAD_REQUEST, "Invalid calendar payload.")
}