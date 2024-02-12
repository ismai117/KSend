import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }

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
        watchosX64(),
        watchosArm32(),
        watchosArm64(),
        watchosSimulatorArm64(),
        tvosX64(),
        tvosArm64(),
        tvosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "lib"
            isStatic = true
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.bundles.ktor.common)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.androidx.activityCompose)
                api(libs.androidx.appcompat)
                api(libs.androidx.core.ktx)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.ktor.client.android)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val watchosX64Main by getting
        val watchosArm32Main by getting
        val watchosArm64Main by getting
        val watchosSimulatorArm64Main by getting
        val tvosX64Main by getting
        val tvosArm64Main by getting
        val tvosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            watchosX64Main.dependsOn(this)
            watchosArm32Main.dependsOn(this)
            watchosArm64Main.dependsOn(this)
            watchosSimulatorArm64Main.dependsOn(this)
            tvosX64Main.dependsOn(this)
            tvosArm64Main.dependsOn(this)
            tvosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.ktor.client.java)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

    }

}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.ksend"

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

mavenPublishing {
//    publishToMavenCentral(SonatypeHost.DEFAULT)
    // or when publishing to https://s01.oss.sonatype.org
    publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)
    signAllPublications()
    coordinates("io.github.ismai117", "KSend", "1.1.2")

    pom {
        name.set(project.name)
        description.set("A description of what my library does.")
        inceptionYear.set("2024")
        url.set("https://github.com/username/mylibrary/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("ismai117")
                name.set("ismai117")
                url.set("https://github.com/ismai117/")
            }
        }
        scm {
            url.set("https://github.com/ismai117/KSend/")
            connection.set("scm:git:git://github.com/ismai117/KSend.git")
            developerConnection.set("scm:git:ssh://git@github.com/ismai117/KSend.git")
        }
    }
}