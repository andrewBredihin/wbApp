import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
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

   buildFeatures {
       buildConfig = true
       compose = true
   }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            val key: String = gradleLocalProperties(rootDir).getProperty("MAP_API_KEY")
            buildConfigField("String", "MAP_API_KEY", key)
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

val compose = "1.5.8"
val composeConstraintLayout = "1.0.1"
val composeBom = "2024.01.00"

dependencies {

    // libs
    implementation(libs.recycler)
    implementation(libs.constraint)
    implementation(libs.viewpager2)
    implementation(libs.map)

    // Room
    implementation(libs.roomMain)
    annotationProcessor(libs.roomAP)
    ksp(libs.roomKSP)

    // Compose
    implementation(platform("androidx.compose:compose-bom:$composeBom"))
    debugImplementation(libs.compose.tooling)
    implementation(libs.bundles.compose)

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
    implementation(project(":ui"))
}