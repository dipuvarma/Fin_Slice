plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.20"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dipuguide.finslice"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dipuguide.finslice"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //For google font
    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.2")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    //dagger hilt
    implementation("com.google.dagger:hilt-android:2.56.1")
    ksp("com.google.dagger:hilt-android-compiler:2.56.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //for extended icon
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")

    //for Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependencies for the Credential Manager libraries and specify their versions
    implementation("com.firebaseui:firebase-ui-auth:9.0.0")
    implementation("com.google.firebase:firebase-firestore")

    //for local dataStore
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    implementation ("com.google.code.gson:gson:2.13.1")

    implementation("androidx.compose.material3:material3:1.4.0-alpha15")

    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

    implementation ("com.google.accompanist:accompanist-swiperefresh:0.30.1")

    implementation("androidx.biometric:biometric:1.4.0-alpha02")

    //for pie chart
    implementation ("io.github.ehsannarmani:compose-charts:0.1.7")

    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")





}