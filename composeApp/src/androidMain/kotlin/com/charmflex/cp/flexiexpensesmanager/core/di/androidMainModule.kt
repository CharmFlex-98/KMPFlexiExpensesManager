package com.charmflex.cp.flexiexpensesmanager.core.di
import com.charmflex.cp.flexiexpensesmanager.core.storage.AndroidFileStorage
import com.charmflex.cp.flexiexpensesmanager.core.storage.AndroidSharedPrefs
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.DocumentManager
import com.charmflex.cp.flexiexpensesmanager.core.storage.FileStorage
import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker
import com.charmflex.cp.flexiexpensesmanager.core.tracker.PostHogEventTracker
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AndroidAssetReader
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AndroidDocumentManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AssetReader
import com.charmflex.cp.flexiexpensesmanager.features.auth.service.device.AndroidDeviceIdGenerator
import com.charmflex.cp.flexiexpensesmanager.features.auth.service.device.DeviceIdGenerator
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val androidMainModule = module {
    singleOf(::PostHogEventTracker) { bind<EventTracker>() }
    singleOf(::AndroidSharedPrefs) { bind<SharedPrefs>() }
    singleOf(::AndroidFileStorage) { bind<FileStorage>() }
    singleOf(::AndroidAssetReader) { bind<AssetReader>() }
    singleOf(::AndroidDocumentManager) { bind<DocumentManager>() }
    singleOf(::AndroidDeviceIdGenerator) { bind<DeviceIdGenerator>() }
}