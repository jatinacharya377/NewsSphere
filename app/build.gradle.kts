plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlinKaptAndroid)
}

android {
    namespace = "com.news.newssphere"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.news.newssphere"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/\"")
            dimension = "env"
        }
        create("prod") {
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/\"")
            dimension = "env"
        }
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
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
//coil
    implementation(libs.androidx.coil.compose)
    //constraint-layout
    implementation(libs.compose.constraintlayout)
    //coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //hilt
    implementation(libs.android.hilt)
    kapt(libs.android.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.compose)
    //navigation
    implementation(libs.androidx.navigation)
    //retrofit
    implementation(libs.sqaureup.retrofit)
    implementation(libs.sqaureup.retrofit.gson.converter)
    implementation(libs.sqaureup.okhttp)
    implementation(libs.sqaureup.okhttp.logging.interceptor)
    //room
    implementation(libs.androidx.room)
    kapt(libs.androidx.room.compiler)
    implementation(libs.room.ktx)
    //viewmodel
    implementation(libs.androidx.compose.viewmodel)
    //work
    implementation(libs.androidx.work.runtime)
}