plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.rickandmorty"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rickandmorty"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments += mapOf(
            "runnerBuilder" to "de.mannodermaus.junit5.AndroidJUnitRunnerBuilder"
        )
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // ========== БАЗОВЫЕ ЗАВИСИМОСТИ ANDROID ==========
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Lifecycle для Service
    implementation("androidx.lifecycle:lifecycle-service:2.7.0")

    // ========== COMPOSE ==========
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Compose ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Compose для LiveData/StateFlow
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0")

    // ========== DEPENDENCY INJECTION (Hilt) ==========
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Для Hilt WorkManager (ОБЯЗАТЕЛЬНО!)
    implementation("androidx.hilt:hilt-work:1.1.0")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    // ========== DATABASE (Room) ==========
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // ========== NETWORK (Retrofit + GSON) ==========
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation("com.google.code.gson:gson:2.10.1")

    // ========== ASYNCHRONOUS (Coroutines) ==========
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // ========== IMAGES (Coil) ==========
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ========== WORK MANAGER (ОБЯЗАТЕЛЬНО!) ==========
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    // Для тестирования WorkManager
    androidTestImplementation("androidx.work:work-testing:2.9.0")

    // ========== SERVICE & NOTIFICATIONS ==========
    implementation("androidx.core:core:1.12.0")

    // ========== UTILS ==========
    // Дата/Время
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // DataStore (для настроек)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // ========== ТЕСТИРОВАНИЕ ==========
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Тестирование WorkManager
    testImplementation("androidx.work:work-testing:2.9.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.51")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Дополнительная конфигурация для Hilt
kapt {
    correctErrorTypes = true
}