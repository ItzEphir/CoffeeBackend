package com.ephirium.coffee_backend.core.hashing.di

import com.ephirium.coffee_backend.core.hashing.service.HashingService
import com.ephirium.coffee_backend.core.hashing.service.Sha256HashingService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val hashingModule = module {
    singleOf(::Sha256HashingService) bind HashingService::class
}