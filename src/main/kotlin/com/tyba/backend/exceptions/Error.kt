package com.tyba.backend.exceptions

data class Error(
    val path: String?,
    val code: String,
    val status: String?,
    val message: String

)
