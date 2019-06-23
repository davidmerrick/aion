package com.merricklabs.aion.exceptions

import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException

class InvalidContentTypeException : HttpResponseException {
    constructor() : super(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, "Invalid content type.")
}