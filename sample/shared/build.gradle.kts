import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.lang.System.load
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.buildConfig)
}

kotlin {
    androidTarget()

    jvm("desktop")

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                api(project(":lib"))


//                implementation("io.github.ismai117:kottie:1.4.1")

            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.8.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.12.0")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
            }
        }

    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
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