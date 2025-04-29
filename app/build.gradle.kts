import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ktlint)
}

ktlint {
    android = true // Enable Android-specific linting rules
    ignoreFailures = false // Fail the build if KtLint finds any issues

    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
    }

    reporters {
        reporter(ReporterType.PLAIN) // Output KtLint results in plain text format
        reporter(ReporterType.HTML) // Output KtLint results in HTML format
    }
}

android {
    namespace = "com.mikedg.thepinballapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mikedg.thepinballapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Read from local.properties
        val localProperties = Properties()
        if (rootProject.file("local.properties").exists()) {
            localProperties.load(FileInputStream(rootProject.file("local.properties")))
        }
        val opdbApiToken = localProperties.getProperty("opdb.api.token")
        if (opdbApiToken.isNullOrEmpty()) {
            throw GradleException("The 'opdb.api.token' property must be defined in local.properties")
        }
        val openAiApiToken = localProperties.getProperty("openai.api.token")
        if (openAiApiToken.isNullOrEmpty()) {
            throw GradleException("The 'openai.api.token' property must be defined in local.properties")
        }
        buildConfigField("String", "OPENAI_API_TOKEN", "\"$openAiApiToken\"")
        buildConfigField("String", "OPDB_API_TOKEN", "\"$opdbApiToken\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.material3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.retrofit)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlin.test)

    // CameraX core libraries
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.androidx.camera.compose)

//    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")
//    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
    implementation(libs.accompanist.permissions)
//    implementation(platform("com.aallam.openai:openai-client-bom:4.0.1"))

    implementation("com.aallam.openai:openai-client:4.0.1")
    runtimeOnly("io.ktor:ktor-client-okhttp:2.3.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // Use the latest version
    
    // Room dependencies
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)
}
