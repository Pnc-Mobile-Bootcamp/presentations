plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.pnc.jetpackcomposedemos.core.common"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
