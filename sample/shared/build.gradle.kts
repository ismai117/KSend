import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.lang.System.load

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.buildConfig)
}

kotlin {
    androidTarget()

    jvm("desktop")

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

                implementation(libs.mvvm.core)
                implementation(libs.mvvm.compose)

                api(libs.precompose.navigation)


            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")
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


buildConfig {
    buildConfigField("String", "API_KEY", "\"${loadProperties("local.properties").getProperty("API_KEY")}\"")
    buildConfigField("String", "ACCOUNTSID", "\"${loadProperties("local.properties").getProperty("ACCOUNTSID")}\"")
    buildConfigField("String", "AUTHTOKEN", "\"${loadProperties("local.properties").getProperty("AUTHTOKEN")}\"")
    buildConfigField("String", "SENDER_EMAIL_ADDRESS", "\"${loadProperties("local.properties").getProperty("SENDER_EMAIL_ADDRESS")}\"")
    buildConfigField("String", "SENDER_PHONE_NUMBER", "\"${loadProperties("local.properties").getProperty("SENDER_PHONE_NUMBER")}\"")
}
