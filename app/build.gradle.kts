plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 33

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.billOfMaterials))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.uiToolingPreview)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.lifecycle)
    implementation(libs.google.android.material)
    implementation(libs.network.retrofit)
    implementation(libs.network.serializationJson)
    implementation(libs.network.serializationConverter)
    implementation(libs.network.okHttp)
    implementation(libs.network.okHttpLoggingInterceptor)
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.splashscreen)
    implementation(libs.google.playServices.location)
    implementation(libs.coroutines.playService)
    implementation(libs.kotlinx.datetime)
    implementation(libs.dataStore)
    testImplementation(libs.testing.mockk)
    testImplementation(libs.testing.mockk.android)
    testImplementation(libs.testing.junit5.bom)
    testImplementation(libs.testing.junit5.engine)
    testImplementation(libs.testing.junit5.engine.vintage)
    testImplementation(libs.testing.turbine)
    testImplementation(libs.testing.coroutines)
    testImplementation(libs.testing.kotest)
    kapt(libs.hilt.android.compiler)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}