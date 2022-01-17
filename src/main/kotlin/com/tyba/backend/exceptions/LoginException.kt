package com.tyba.backend.exceptions

class LoginException(var code: String, var messageArguments: Array<Any>? = null) : RuntimeException()
