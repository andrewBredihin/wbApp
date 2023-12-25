plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.bav.wbapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.bav.wbapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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

    viewBinding {
        enable = true
    }
}

dependencies {

    // libs
    implementation(libs.recycler)
    implementation(libs.constraint)

    // bundles-libs
    implementation(libs.bundles.common)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.lifecycle)
    androidTestImplementation(libs.bundles.test)

    implementation(project(":core"))
    implementation(project(":core-ui"))
}