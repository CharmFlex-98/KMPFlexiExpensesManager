package com.charmflex.cp.flexiexpensesmanager.core.utils.di

import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

internal inline fun <reified T: Any> getDep(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    return AppComponentProvider.instance.getAppComponent().getDep<T>()
}