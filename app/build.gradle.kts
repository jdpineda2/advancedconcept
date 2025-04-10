plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.appdistribution)

}

android {
    namespace = "com.example.advancedconceptsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.advancedconceptsapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true // Should be true by default for release
            isShrinkResources = true // R8 handles both
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro" // Our custom rules file
            )
        }
        getByName("debug") {
            isMinifyEnabled = false // Usually false for debug
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        firebaseAppDistribution {
            artifactType = "APK"
            releaseNotes = "Latest build with fixes/features"
            testers = "jdpinedahernandez@gmail.com, bri@example.com, cal@example.com"
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
    buildFeatures {
        buildConfig = true // Explicitly enable BuildConfig generation
    }
    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev" // e.g., com.yourcompany.advancedconceptsapp.dev
            versionNameSuffix = "-dev"
            // Define build config fields or res values specific to dev
            resValue("string", "app_name", "Task Reporter (Dev)")
            buildConfigField("String", "TASKS_COLLECTION", "\"tasks_dev\"") // Note escaping quotes
            buildConfigField("String", "USAGE_LOG_COLLECTION", "\"usage_logs_dev\"")
        }
        create("prod") {
            dimension = "environment"
            // No suffixes for production
            resValue("string", "app_name", "Task Reporter")
            buildConfigField("String", "TASKS_COLLECTION", "\"tasks\"")
            buildConfigField("String", "USAGE_LOG_COLLECTION", "\"usage_logs\"")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.viewmodel)
    implementation(libs.firebase.firestore)
    implementation(libs.workManager)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}