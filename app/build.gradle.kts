import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

// Lee keystore.properties si existe (desarrollo local)
val keystoreProps = Properties()
val keystoreFile = rootProject.file("keystore.properties")
if (keystoreFile.exists()) keystoreProps.load(keystoreFile.inputStream())

android {
    namespace   = "com.petdoc.app"
    compileSdk  = 34

    defaultConfig {
        applicationId   = "com.petdoc.app"
        minSdk          = 26
        targetSdk       = 34
        versionCode     = (System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: 1)
        versionName     = "2.1.${System.getenv("BUILD_NUMBER") ?: "0"}"
    }

    // ✅ MÓDULO 4: Configuración de firma para APK firmada
    signingConfigs {
        create("release") {
            storeFile   = file(System.getenv("KEYSTORE_PATH") ?: keystoreProps["storeFile"] ?: "release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: keystoreProps["storePassword"]?.toString() ?: ""
            keyAlias    = System.getenv("KEY_ALIAS")           ?: keystoreProps["keyAlias"]?.toString()       ?: ""
            keyPassword = System.getenv("KEY_PASSWORD")        ?: keystoreProps["keyPassword"]?.toString()    ?: ""
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled   = false  // false para pruebas rápidas
            signingConfig     = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable        = true
        }
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines)

    // Compose
    val bom = platform(libs.androidx.compose.bom)
    implementation(bom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Navigation + Hilt
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // WorkManager + Hilt-Work
    implementation(libs.androidx.work.runtime)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)

    // QR
    implementation(libs.zxing.core)
    implementation(libs.zxing.android)

    // Maps & Location
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Image & Utils
    implementation(libs.glide)
    implementation(libs.gson)
}
