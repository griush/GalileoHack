plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.gnsstracker.mainapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gnsstracker.mainapp"
        minSdk = 29
        targetSdk = 35
        versionCode = 5
        versionName = "1.0.0-b3"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.play.services.maps)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)
    implementation(libs.play.services.location)
    implementation(libs.jackson.databind)
    implementation(libs.core.splashscreen)
    implementation(libs.osmdroid.android)
}


