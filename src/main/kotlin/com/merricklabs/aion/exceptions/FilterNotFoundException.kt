package com.merricklabs.aion.exceptions

import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException

class FilterNotFoundException : HttpResponseException {
    constructor(id: String) : super(HttpStatus.SC_NOT_FOUND, "Filter with id $id not found.")
}