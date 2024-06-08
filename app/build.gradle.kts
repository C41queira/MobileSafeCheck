plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.batatinhas.safechecktest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.batatinhas.safechecktest"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /***Dependencias Firebase***/
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))//Plataforma
    implementation("com.google.firebase:firebase-analytics")//Analytics
    implementation("com.google.firebase:firebase-auth-ktx")//Autentificação
    implementation("com.google.firebase:firebase-storage-ktx")//Storage para imagens
    implementation("com.google.firebase:firebase-firestore")//Firestore
    implementation("com.google.firebase:firebase-database-ktx")//Realtime Database
}