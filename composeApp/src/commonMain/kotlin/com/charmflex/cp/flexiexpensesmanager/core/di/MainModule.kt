package com.charmflex.cp.flexiexpensesmanager.core.di

import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigatorImpl
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

//import com.charmflex.cp.flexiexpensesmanager.core.network.DefaultNetworkClientBuilder
//import com.charmflex.cp.flexiexpensesmanager.core.network.NetworkClientBuilder
//import com.charmflex.cp.flexiexpensesmanager.features.backup.TransactionBackupManager
//import com.charmflex.flexiexpensesmanager.features.backup.TransactionBackupManagerImpl
//import com.charmflex.cp.flexiexpensesmanager.core.storage.FileStorage
//import com.charmflex.cp.flexiexpensesmanager.core.storage.FileStorageImpl
//import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
//import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefsImpl
//import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker
//import com.charmflex.cp.flexiexpensesmanager.core.tracker.PostHogEventTracker
//import com.charmflex.cp.flexiexpensesmanager.core.utils.file.DocumentManager
//import com.charmflex.cp.flexiexpensesmanager.core.utils.FEFileProviderImpl
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.Dispatchers
//import org.koin.core.module.dsl.singleOf
//import org.koin.dsl.module
//
//@Module(
//    includes = [
//        NetworkModule::class
//    ]
//)
//internal interface MainModule {
//
//    @Binds
//    @Singleton
//    fun bindsSharedPrefs(sharedPrefsImpl: SharedPrefsImpl): SharedPrefs
//
//    @Binds
//    @Singleton
//    fun bindsFileStorage(fileStorageImpl: FileStorageImpl): FileStorage
//
//    fun bindsBackupManager(transactionBackupManagerImpl: TransactionBackupManagerImpl): TransactionBackupManager
//
//    fun bindsFEFileProvider(feFileProviderImpl: FEFileProviderImpl): DocumentManager
//
//    companion object {
//        @Dispatcher(dispatcherType = Dispatcher.Type.IO)
//        fun providesDispatcherIO(): CoroutineDispatcher {
//            return Dispatchers.IO
//        }
//
//        fun providesEventTracker(appContext: Context): EventTracker {
//            val res = PostHogEventTracker(appContext)
//            res.init()
//
//            return res;
//        }
//    }
//}

@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcherType: Type) {
    enum class Type {
        IO, DEFAULT
    }
}

val mainModule = module {
    singleOf(::ResourcesProvider)
    singleOf(::RouteNavigatorImpl) { bind<RouteNavigator>() }
//    singleOf(::FileStorageImpl) {  }
//    singleOf(::DefaultNetworkClientBuilder) { bind<NetworkClientBuilder>() }
//    singleOf(::TransactionBackupManagerImpl) { }
}