import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.serialization)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "pl.ceranka.weather4you"
    compileSdk = 35

    defaultConfig {
        applicationId = "pl.ceranka.weather4you"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = getLocalProperties()
        val apiKey = localProperties.getProperty("OPEN_WEATHER_API_KEY")
        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    //Serialization
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    //Coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
    //Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    //implementation("androidx.room:room-coroutines:2.1.0-alpha04")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //TODO: remove
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
}

kapt {
    correctErrorTypes = true
}

private fun getLocalProperties(): Properties {
    val propertiesFile = project.rootProject.file("local.properties")
    val properties = Properties().apply { load(propertiesFile.inputStream()) }
    return properties
}