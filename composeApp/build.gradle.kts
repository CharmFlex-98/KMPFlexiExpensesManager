import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.google.services)
    alias(libs.plugins.google.firebase.crashlytics.plugin)
    alias(libs.plugins.kotlin.serialization.plugin)
    alias(libs.plugins.room.gradle.plugin)
    alias(libs.plugins.ksp.plugin)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.ui.text.google.fonts)
            implementation(compose.preview)
//            implementation(libs.androidx.activity.compose)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            implementation(project.dependencies.platform(libs.google.firebase.bom))
            implementation(libs.google.firebase.crashlytics)
            implementation(libs.google.firebase.analytics)
            implementation(libs.google.identity.id)
            implementation(libs.androidx.play.services.auth)
            implementation(libs.google.firebase.auth)
            implementation(libs.apache.poi)
            implementation(libs.apache.poi.ooxml)

            // Calendar related
            implementation(libs.compose.dialog.calendar)
            implementation(libs.compose.dialog.state)
            implementation(libs.compose.dialog.clock)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.posthog)

            implementation(libs.google.billing)
            
            implementation(libs.androidx.security.crypto)
            implementation(libs.compose.cloudy)
        }
        commonMain.dependencies {
//            // crypto
//            implementation("dev.whyoleg.cryptography:cryptography-core:0.5.0")
//            implementation("dev.whyoleg.cryptography:cryptography-provider-optimal:0.5.0")

            // ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.nego)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)

            implementation(libs.aay.chart)

            implementation(libs.fluid.currency)
            implementation(libs.compottie)
            implementation(libs.compottie.dot)
            implementation(libs.compottie.network)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            api(libs.koin.annotation)



            implementation(libs.androidx.savedState)
            implementation(libs.compose.calendar)
//            implementation(libs.vico.core)
//            implementation(libs.vico.compose.m3)
            implementation(libs.vico.multiplatform)
            implementation(libs.vico.multiplatform.m3)
            implementation(libs.vico.core)
            implementation(libs.coil)

            implementation(compose.material3)
            implementation(libs.kotlin.serialization)
            implementation(libs.room)
            implementation(libs.sqlite.bundled)
            implementation(libs.compose.navigation.backhandler)
            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.datetime)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.core.coroutine)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
        }
    }

    // KSP Common sourceSet
    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

// Trigger Common Metadata Generation from Native tasks
project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

android {
    namespace = "com.charmflex.cp.flexiexpensesmanager"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    flavorDimensions += "version"

    productFlavors {
        create("free") {
            dimension = "version"
            applicationIdSuffix = ".free"
            versionNameSuffix = "-free"
            // You can add custom build config fields
            buildConfigField("boolean", "IS_PAID_VERSION", "false")
            buildConfigField("String", "FLAVOR_NAME", "\"free\"")
        }

        create("paid") {
            dimension = "version"
            applicationIdSuffix = ".premium"
            versionNameSuffix = "-premium"
            buildConfigField("boolean", "IS_PAID_VERSION", "true")
            buildConfigField("String", "FLAVOR_NAME", "\"paid\"")
        }
    }

    defaultConfig {
        applicationId = "com.charmflex.cp.flexiexpensesmanager"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 11
        versionName = "1.0.11"
    }


    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"  // This would add .debug to all debug builds
            buildConfigField("String", "SERVER_URL", "\"http://10.0.2.2:8080\"")
            buildConfigField("String", "PUB_SIGN_PEM_PATH", "\"crypto/pub_sign_debug.pem\"")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            buildConfigField("String", "SERVER_URL", "\"https://fem.charmflex.com\"")
            buildConfigField("String", "PUB_SIGN_PEM_PATH", "\"crypto/pub_sign_debug.pem\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Remove isDebuggable = true for proper release builds
            // Remove debug signing for production releases
//             isDebuggable = true
//             signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.ui.android)
    debugImplementation(compose.uiTooling)
    add("kspAndroid", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
//
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
}

