import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.file("config/detekt/detekt.yml"))
    ignoreFailures = true   // Jenkins decides pass/fail, not Gradle
}

ktlint {
    android.set(true)
    baseline.set(rootProject.file("config/ktlint/baseline.xml"))
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
}

room {
    schemaDirectory("${projectDir}/schemas")
}

composeCompiler {
    reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
    metricsDestination.set(layout.buildDirectory.dir("compose_compiler"))
    stabilityConfigurationFiles.add(layout.projectDirectory.file("compose_stability.conf"))
}

android {
    namespace = "com.pnc.jetpackcomposedemos"
    compileSdk {
        version = release(37)
    }

    lint {
        checkTestSources = false
    }

    defaultConfig {
        applicationId = "com.pnc.jetpackcomposedemos"
        minSdk = 26
        targetSdk = 37
        val ciBuild = System.getenv("BUILD_NUMBER")?.toIntOrNull()
        versionCode = ciBuild ?: 1
        versionName = "1.0." + (ciBuild ?: 0)

        testInstrumentationRunner = "com.pnc.jetpackcomposedemos.HiltTestRunner"
        buildConfigField("String", "BASE_URL", "\"https://kazoopromotions.com/api/\"")
        buildConfigField("String", "AW_BASE_URL", "\"https://api.bootcampcentral.com/api/\"")
        buildConfigField("String", "AUTH_BASE_URL", "\"https://api.bootcampcentral.com/\"")
        buildConfigField("String", "OIDC_REDIRECT_URI", "\"com.pnc.jetpackcomposedemos:/oauth2redirect\"")
        buildConfigField("String", "OIDC_SCOPES", "\"openid profile offline_access orders.read\"")
        buildConfigField("String", "OIDC_CLIENT_ID", "\"adventureworks-mobile\"")
    }

    androidResources {
        generateLocaleConfig = true
    }

    signingConfigs {
        // Only defined when Jenkins (or you) provide the keystore through environment variables
        val keystorePath = System.getenv("KEYSTORE_FILE")
        if (keystorePath != null) {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug {
            isPseudoLocalesEnabled = true
            enableUnitTestCoverage = true
        }
        release {
            signingConfigs.findByName("release")?.let { signingConfig = it }
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    sourceSets {
        getByName("androidTest") {
            assets.directories.add("schemas")
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":feature:todo"))
    implementation(project(":core:designsystem"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.bundles.coil)
    implementation(libs.androidx.compose.viewbinding)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.bundles.lifecycle)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.okhttp)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.room.testing)
    implementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(platform(libs.kotlinx.coroutines.bom))
    androidTestImplementation(platform(libs.kotlinx.coroutines.bom))
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.paging.compose)
    testImplementation(libs.androidx.paging.testing)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.timber)
}

configurations.configureEach {
    resolutionStrategy {
        force(libs.androidx.espresso.core)
    }
}
