package com.ephirium.coffee_backend.feature.auth.di

import com.ephirium.coffee_backend.data.password.di.passwordDataModule
import com.ephirium.coffee_backend.data.user.di.userDataModule
import com.ephirium.coffee_backend.feature.auth.controller.AuthController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authFeatureModule = module {
    includes(userDataModule, passwordDataModule)

    singleOf(::AuthController)
}