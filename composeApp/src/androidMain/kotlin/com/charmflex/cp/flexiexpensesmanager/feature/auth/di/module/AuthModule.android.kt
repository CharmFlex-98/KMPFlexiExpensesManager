package com.charmflex.cp.flexiexpensesmanager.feature.auth.di.module

import com.charmflex.cp.flexiexpensesmanager.feature.auth.service.sign_in.GoogleSignInService
import com.charmflex.cp.flexiexpensesmanager.features.auth.service.sign_in.SignInService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModuleAndroid = module {
    singleOf(::GoogleSignInService) { bind<SignInService>() }
    single { Firebase.auth }.bind<FirebaseAuth>()
}