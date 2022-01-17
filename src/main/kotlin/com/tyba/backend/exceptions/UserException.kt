package com.tyba.backend.exceptions

class UserException(var code: String, var messageArguments: Array<Any>? = null) : RuntimeException()
