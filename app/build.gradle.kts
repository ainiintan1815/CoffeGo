plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.coffeeshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.coffeeshop"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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

    buildFeatures { viewBinding = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Firebase (Realtime DB)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-database-ktx")



    // App Check dependencies
    implementation ("com.google.firebase:firebase-appcheck")
    implementation ("com.google.firebase:firebase-appcheck-debug")
    implementation ("com.google.firebase:firebase-appcheck-playintegrity")

    implementation("com.google.firebase:firebase-appcheck-debug:17.1.2")
    implementation("com.google.firebase:firebase-appcheck-playintegrity:17.1.2")

    // Lifecycle (kalau dipakai ViewModel/LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")

    // Glide & Gson
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation(libs.androidx.activity)

    implementation("com.midtrans:uikit:2.4.0-SANDBOX")

    implementation("com.google.firebase:firebase-auth:22.3.0")

    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.android.gms:play-services-auth:20.1.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.activity:activity-ktx:1.3.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("androidx.datastore:datastore-core:1.1.1")



    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")



    implementation("androidx.fragment:fragment-ktx:1.8.4")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.6.0")


    implementation("androidx.work:work-runtime:2.8.1")
    implementation("com.loopj.android:android-async-http:1.4.10")

}
