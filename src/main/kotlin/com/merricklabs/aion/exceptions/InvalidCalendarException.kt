package com.merricklabs.aion.exceptions

import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException

class InvalidCalendarException : HttpResponseException {
    constructor(url: String, message: String) : super(HttpStatus.SC_BAD_REQUEST, "Invalid calendar url: $url. Error: $message")
}