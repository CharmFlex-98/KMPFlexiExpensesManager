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
            implementation(libs.androidx.activity.compose)
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

            implementation(libs.androidx.security.crypto)
        }
        commonMain.dependencies {
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
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

    defaultConfig {
        applicationId = "com.charmflex.cp.flexiexpensesmanager"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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

