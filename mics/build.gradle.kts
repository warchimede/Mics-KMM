import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

kotlin {
    jvm()
    android()

    val baseName = "Mics"
    val iosArm64 = iosArm64()
    val iosX64 = iosX64()
    val iosSimulatorArm64 = iosSimulatorArm64()
    val tvosArm64 = tvosArm64()
    val tvosX64 = tvosX64()
    val tvosSimulatorArm64 = tvosSimulatorArm64()
    configure(listOf(
        iosArm64, iosX64, iosSimulatorArm64,
        tvosArm64, tvosX64, tvosSimulatorArm64
    )) {
        binaries.framework {
            this.baseName = baseName
        }
    }

    val release = "RELEASE"
    val iosArm64BinariesTask = "iosArm64Binaries"
    val iosX64BinariesTask = "iosX64Binaries"
    val iosSimulatorArm64BinariesTask = "iosSimulatorArm64Binaries"
    val tvosArm64BinariesTask = "tvosArm64Binaries"
    val tvosX64BinariesTask = "tvosX64Binaries"
    val tvosSimulatorArm64BinariesTask = "tvosSimulatorArm64Binaries"

    // Create tasks to build fat frameworks for simulators, mandatory for xcframework generation
    val releaseIOSSimulatorsFatFrameworkTask = "releaseIOSSimulatorsFatFramework"
    tasks.register<FatFrameworkTask>(releaseIOSSimulatorsFatFrameworkTask) {
        dependsOn(iosX64BinariesTask, iosSimulatorArm64BinariesTask)

        // The fat framework must have the same base name as the initial frameworks.
        this.baseName = baseName
        destinationDir = buildDir.resolve("bin/iosFatSimulator/releaseFramework/")

        // Specify the frameworks to be merged.
        from(
            iosX64.binaries.getFramework(release),
            iosSimulatorArm64.binaries.getFramework(release)
        )
    }

    val releaseTVOSSimulatorsFatFrameworkTask = "releaseTVOSSimulatorsFatFramework"
    tasks.register<FatFrameworkTask>(releaseTVOSSimulatorsFatFrameworkTask) {
        dependsOn(tvosX64BinariesTask, tvosSimulatorArm64BinariesTask)

        // The fat framework must have the same base name as the initial frameworks.
        this.baseName = baseName
        destinationDir = buildDir.resolve("bin/tvosFatSimulator/releaseFramework/")

        // Specify the frameworks to be merged.
        from(
            tvosX64.binaries.getFramework(release),
            tvosSimulatorArm64.binaries.getFramework(release)
        )
    }

    // Create task to build XCFramework
    val releaseXCFrameworkTask = "releaseXCFramework"
    tasks.register(releaseXCFrameworkTask) {
        dependsOn(
            iosArm64BinariesTask, releaseIOSSimulatorsFatFrameworkTask,
            tvosArm64BinariesTask, releaseTVOSSimulatorsFatFrameworkTask
        )

        doLast {
            exec {
                val iosArm64Path = iosArm64.binaries.getFramework(release).outputFile.path
                val iosSimPath = tasks[releaseIOSSimulatorsFatFrameworkTask].outputs.files.singleFile.path + "/$baseName.framework"
                val tvosArm64Path = tvosArm64.binaries.getFramework(release).outputFile.path
                val tvosSimPath = tasks[releaseTVOSSimulatorsFatFrameworkTask].outputs.files.singleFile.path + "/$baseName.framework"
                val xcframeworkPath = buildDir.resolve("bin/XCFramework/releaseFramework/$baseName.xcframework")
                val frameworkOpt = "-framework"

                commandLine("xcodebuild", "-create-xcframework",
                    frameworkOpt, iosArm64Path,
                    frameworkOpt, iosSimPath,
                    frameworkOpt, tvosArm64Path,
                    frameworkOpt, tvosSimPath,
                    "-output", xcframeworkPath
                )
            }
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Coroutines.core)
                implementation(Dependencies.Serialization.core)
                implementation(Dependencies.Ktor.core)
                implementation(Dependencies.Ktor.serialization)
                implementation(Dependencies.Ktor.logging)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(Dependencies.Coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.androidClient)
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.Ktor.iosClient)
            }
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosX64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val tvosArm64Main by getting {
            dependsOn(iosMain)
        }

        val tvosX64Main by getting {
            dependsOn(iosMain)
        }

        val tvosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val jvmTest by getting {
            dependencies {
                implementation(Dependencies.Mockk.core)
                implementation(Dependencies.Mockk.jvmAgent)
            }
        }

        val androidTest by getting {
            dependsOn(jvmTest)
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}