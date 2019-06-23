package com.merricklabs.aion.exceptions

import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException

class InvalidAcceptHeadersException : HttpResponseException {
    constructor() : super(HttpStatus.SC_NOT_ACCEPTABLE, "Invalid headers.")
}