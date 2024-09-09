package com.ephirium.coffee_backend.core.config

class UndefinedApplicationConfigException(field: String): Exception("Field '$field' is not defined")