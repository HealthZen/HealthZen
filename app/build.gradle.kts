plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.healthzensignuplogin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.healthzensignuplogin"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "YOUTUBE_API_KEY", "\"AIzaSyA6pMQthbKdx5br3FwTm8hImsPlUVpMziM\"")
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

    buildFeatures {
        viewBinding = true
        buildConfig = true

    }


    dependencies {

        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("com.google.firebase:firebase-auth:22.3.1")
        implementation("com.google.firebase:firebase-database:20.3.1")
        implementation("com.google.firebase:firebase-firestore:24.10.3")
        implementation("com.github.dfloureiro:news-api-kotlin:v2.1")
        implementation("io.reactivex.rxjava2:rxjava:2.2.21")
        implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
        implementation("com.squareup.picasso:picasso:2.8")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.google.code.gson:gson:2.8.8")

        implementation("com.github.bumptech.glide:glide:4.12.0")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        implementation ("androidx.navigation:navigation-fragment-ktx:2.3.5")
        implementation ("androidx.navigation:navigation-ui-ktx:2.3.5")

        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    }
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

}


