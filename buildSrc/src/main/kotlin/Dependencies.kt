object Dependencies {

    object GradlePlugins {
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Config.kotlinVersion}"
        const val serialization = "org.jetbrains.kotlin:kotlin-serialization:${Config.kotlinVersion}"
        const val android = "com.android.tools.build:gradle:7.2.0"
    }

    object Coroutines {
        private const val version = "1.6.3-native-mt"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Ktor {
        private const val version = "1.6.8"
        const val core = "io.ktor:ktor-client-core:$version"
        const val serialization = "io.ktor:ktor-client-serialization:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
        const val androidClient = "io.ktor:ktor-client-android:$version"
        const val iosClient = "io.ktor:ktor-client-ios:$version"
    }

    object Mockk {
        private const val version = "1.12.5"
        const val core = "io.mockk:mockk:$version"
        const val jvmAgent = "io.mockk:mockk-agent-jvm:$version"
    }

    object Serialization {
        private const val version = "1.3.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:$version"
    }
}