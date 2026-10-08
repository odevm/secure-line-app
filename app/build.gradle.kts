import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

fun secureLineProp(name: String): String {
    val fromEnv = System.getenv(name)
    if (!fromEnv.isNullOrBlank()) return fromEnv
    val local = Properties()
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { local.load(it) }
    }
    return local.getProperty(name, "") ?: ""
}

val googleServicesFile = file("google-services.json")
val firebaseEnabledProp =
    secureLineProp("SECURELINE_FIREBASE_ENABLED").equals("true", ignoreCase = true)
val applyFirebasePlugins = firebaseEnabledProp && googleServicesFile.exists()

android {
    namespace = "org.secureline.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.secureline.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "org.secureline.app.HiltTestRunner"

        val githubRepoUrl = secureLineProp("SECURELINE_GITHUB_REPO_URL")
        val githubOwner = secureLineProp("SECURELINE_GITHUB_OWNER")
        val githubRepo = secureLineProp("SECURELINE_GITHUB_REPO")
        val telegramUrl = secureLineProp("SECURELINE_TELEGRAM_CHANNEL_URL")
        val firebaseEnabled = applyFirebasePlugins

        buildConfigField("String", "GITHUB_REPO_URL", "\"${githubRepoUrl.replace("\"", "\\\"")}\"")
        buildConfigField("String", "GITHUB_OWNER", "\"${githubOwner.replace("\"", "\\\"")}\"")
        buildConfigField("String", "GITHUB_REPO", "\"${githubRepo.replace("\"", "\\\"")}\"")
        buildConfigField(
            "String",
            "TELEGRAM_CHANNEL_URL",
            "\"${telegramUrl.replace("\"", "\\\"")}\"",
        )
        buildConfigField("boolean", "FIREBASE_ENABLED", firebaseEnabled.toString())
    }

    val keystorePropsFile = rootProject.file("keystore.properties")
    val keystoreProps = Properties()
    if (keystorePropsFile.exists()) {
        keystorePropsFile.inputStream().use { keystoreProps.load(it) }
    }

    signingConfigs {
        if (keystorePropsFile.exists()) {
            create("release") {
                storeFile = rootProject.file(keystoreProps["storeFile"] as String)
                storePassword = keystoreProps["storePassword"] as String
                keyAlias = keystoreProps["keyAlias"] as String
                keyPassword = keystoreProps["keyPassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            if (keystorePropsFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    externalNativeBuild {
        cmake {
            path = file("../native/modem/CMakeLists.txt")
        }
    }

    ndkVersion = "28.2.13676358"

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "17"
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.3")

    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.crypto.tink:tink-android:1.15.0")

    if (applyFirebasePlugins) {
        implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-messaging")
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-crashlytics")
        implementation("com.google.android.gms:play-services-auth:21.2.0")
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("org.json:json:20240303")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.52")
    kspAndroidTest("com.google.dagger:hilt-compiler:2.52")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

if (applyFirebasePlugins) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}
