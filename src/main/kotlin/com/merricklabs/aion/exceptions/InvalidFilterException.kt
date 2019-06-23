package com.merricklabs.aion.exceptions

import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException

class InvalidFilterException : HttpResponseException {
    constructor() : super(HttpStatus.SC_BAD_REQUEST, "Invalid filter payload.")
}