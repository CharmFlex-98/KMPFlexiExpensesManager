package com.charmflex.cp.flexiexpensesmanager.core.di
import android.media.metrics.Event
import com.charmflex.cp.flexiexpensesmanager.core.app.AndroidAppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.core.app.AppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.core.crypto.AndroidBase64Manager
import com.charmflex.cp.flexiexpensesmanager.core.crypto.AndroidSignatureVerifier
import com.charmflex.cp.flexiexpensesmanager.core.crypto.Base64Manager
import com.charmflex.cp.flexiexpensesmanager.core.crypto.SignatureVerifier
import com.charmflex.cp.flexiexpensesmanager.core.storage.AndroidFileStorage
import com.charmflex.cp.flexiexpensesmanager.core.storage.AndroidSharedPrefs
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.DocumentManager
import com.charmflex.cp.flexiexpensesmanager.core.storage.FileStorage
import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker
import com.charmflex.cp.flexiexpensesmanager.core.tracker.PostHogEventTracker
import com.charmflex.cp.flexiexpensesmanager.core.utils.AndroidCurrencyFormatterImpl
import com.charmflex.cp.flexiexpensesmanager.core.utils.AndroidRateExchangeManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.RateExchangeManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AndroidDocumentManager
import com.charmflex.cp.flexiexpensesmanager.di.ActivityProvider
import com.charmflex.cp.flexiexpensesmanager.features.auth.service.device.AndroidDeviceIdGenerator
import com.charmflex.cp.flexiexpensesmanager.features.auth.service.device.DeviceIdGenerator
import com.charmflex.cp.flexiexpensesmanager.features.backup.AndroidAppDataService
import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataService
import com.charmflex.cp.flexiexpensesmanager.features.billing.AndroidBillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.FeatureFlagService
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.FeatureFlagServiceImpl
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.charmflex.cp.flexiexpensesmanager")
class AndroidModule

val androidMainModule = module {
    single { ActivityProvider() }
    singleOf(::AndroidAppConfigProvider) { bind<AppConfigProvider>() }
    single { PostHogEventTracker(get()) }
    singleOf(::PostHogEventTracker) { bind<EventTracker>() }
    singleOf(::AndroidSharedPrefs) { bind<SharedPrefs>() }
    singleOf(::AndroidFileStorage) { bind<FileStorage>() }
    singleOf(::AndroidDocumentManager) { bind<DocumentManager>() }
    singleOf(::AndroidDeviceIdGenerator) { bind<DeviceIdGenerator>() }
    singleOf(::AndroidCurrencyFormatterImpl) { bind<CurrencyFormatter>() }
    singleOf(::AndroidRateExchangeManager) { bind<RateExchangeManager>() }
    singleOf(::FeatureFlagServiceImpl) { bind<FeatureFlagService>() }
    singleOf(::AndroidBillingManager) { bind<BillingManager>() }
    singleOf(::AndroidAppDataService) { bind<AppDataService>()}
    singleOf(::AndroidSignatureVerifier) { bind<SignatureVerifier>() }
    singleOf(::AndroidBase64Manager) { bind<Base64Manager>() }
    AndroidModule().module
}