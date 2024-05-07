plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "com.example.galileomastermindboilerplate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.galileomastermindboilerplate"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.gms:play-services-location:17.0.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    //implementation("com.google.protobuf:protobuf-javalite:3.25.1")
    //implementation("com.google.protobuf.nano:protobuf-javanano:3.1.0")
    implementation("com.google.guava:guava:16.0")
    implementation("com.google.auto.value:auto-value:1.4")
    implementation("joda-time:joda-time:2.3")
    implementation("com.google.gdata:core:1.47.1")
    implementation("com.google.guava:guava:27.0.1-android")

}