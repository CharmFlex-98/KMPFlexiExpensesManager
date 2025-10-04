package com.charmflex.cp.flexiexpensesmanager.di

import org.koin.dsl.KoinConfiguration

expect class KoinInitializer {
    fun init()

    fun initAsync(): KoinConfiguration
}
