package com.ephirium.coffee_backend.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import org.koin.dsl.module

val mainModule = module {
    single {
        MongoClient.create("mongodb://0.0.0.0:27017").getDatabase("CoffeeDatabase")
    }
}