package com.ephirium.coffee_backend.data.user.di

import com.ephirium.coffee_backend.core.hashing.di.hashingModule
import com.ephirium.coffee_backend.core.token.di.tokenModule
import com.ephirium.coffee_backend.data.user.service.MongoUserService
import com.ephirium.coffee_backend.data.user.service.UserService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userDataModule = module {
    includes(hashingModule, tokenModule)

    singleOf(::MongoUserService) bind UserService::class
}