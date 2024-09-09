package com.ephirium.coffee_backend.data.password.di

import com.ephirium.coffee_backend.data.password.service.MongoPasswordService
import com.ephirium.coffee_backend.data.password.service.PasswordService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val passwordDataModule = module {

    singleOf(::MongoPasswordService) bind PasswordService::class
}