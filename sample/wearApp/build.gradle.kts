import java.util.Properties


plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.buildConfig)
}

android {
    namespace = "com.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.myapplication.MyApplication"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
}

dependencies {
    implementation(project(":sample:shared"))
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activityCompose)
    implementation(libs.compose.ui)
    implementation(libs.wear.foundation)
    implementation(libs.wear.material)
    implementation(libs.horologist.compose.material)
    implementation(libs.horologist.compose.layout)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

buildConfig {
    buildConfigField(
        type = "String",
        name = "API_KEY",
        value = "\"${properties.getProperty("API_KEY")}\""
    )
    buildConfigField(
        type = "String",
        name = "ACCOUNTSID",
        value = "\"${properties.getProperty("ACCOUNTSID")}\""
    )
    buildConfigField(
        type = "String",
        name = "AUTHTOKEN",
        value = "\"${properties.getProperty("AUTHTOKEN")}\""
    )
    buildConfigField(
        type = "String",
        name = "SENDER_EMAIL_ADDRESS",
        value = "\"${properties.getProperty("SENDER_EMAIL_ADDRESS")}\""
    )
    buildConfigField(
        type = "String",
        name = "SENDER_PHONE_NUMBER",
        value = "\"${properties.getProperty("SENDER_PHONE_NUMBER")}\""
    )
    buildConfigField(
        type = "String",
        name = "GEMINI_API_KEY",
        value = "\"${properties.getProperty("GEMINI_API_KEY")}\""
    )
}