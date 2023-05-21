import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.shannon.plugin.version.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.shannon.plugin.version")
    id("kotlin-parcelize")
}

private object BuildType {
    const val RELEASE = "release"
    const val DEBUG = "debug"
    const val FOX = "fox"
}

android {
    compileSdk = AndroidConfig.compileSdkVersion
    defaultConfig {
        applicationId = AndroidConfig.applicationId
        minSdk = AndroidConfig.minSdkVersion
        targetSdk = AndroidConfig.targetSdkVersion
        versionCode = AndroidConfig.versionCode
        versionName = AndroidConfig.versionName

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create(BuildType.RELEASE) {
            storeFile = File(SignConfig.keyStoreFile)
            storePassword = SignConfig.keyStorePassword
            keyAlias = SignConfig.keyAlias
            keyPassword = SignConfig.keyPassword

            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        val hostInfo = getHostInfo()
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
            signingConfig = signingConfigs.getByName(BuildType.RELEASE)
            manifestPlaceholders["appName"] = AndroidConfig.appName
            buildConfigField("String", "HOST_URL", hostInfo[0])
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            versionNameSuffix = "-${BuildType.DEBUG}"
            applicationIdSuffix = ".${BuildType.DEBUG}"
            manifestPlaceholders["appName"] = AndroidConfig.appName.plus("-dev")
            buildConfigField("String", "HOST_URL", hostInfo[1])
        }
        create(BuildType.FOX) {
            isMinifyEnabled = false
            isDebuggable = true
            versionNameSuffix = "-${BuildType.FOX}"
            applicationIdSuffix = ".${BuildType.FOX}"
            manifestPlaceholders["appName"] = AndroidConfig.appName.plus("-fox")
            buildConfigField("String", "HOST_URL", hostInfo[2])
        }
    }

    applicationVariants.all {
        val buildType = buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                if (buildType == BuildType.RELEASE) {
                    outputFileName =
                        "OpenVoice_v${versionCode}_${flavorName}_${AndroidConfig.baleDate}.apk"
                }
            }
        }
    }

    flavorDimensions.add(AndroidConfig.appName)
    productFlavors {
        create("official")
        create("huawei")
    }
    productFlavors.all {
        manifestPlaceholders["APP_CHANNEL"] = name
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.activity)
    implementation(Deps.AndroidX.fragment)
    implementation(Deps.AndroidX.recyclerview)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.multidex)

    implementation(Deps.Glide.glide)
    kapt(Deps.Glide.glideCompiler)
    implementation(Deps.Libs.mmkv)
    implementation(Deps.Libs.eventBus)
    implementation(Deps.Libs.startup)
    implementation(Deps.Libs.autoSize)


    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.testExt)
    androidTestImplementation(Deps.Test.testEspresso)

    implementation(project(":lib-core"))
    implementation(project(":lib-player"))

}

fun getHostInfo(): Array<String> {
    val properties = gradleLocalProperties(rootDir)
    val releaseHostUrl = properties.getProperty("releaseHostUrl")
    val debugHostUrl = properties.getProperty("debugHostUrl")
    val foxHostUrl = properties.getProperty("foxHostUrl")

    if (releaseHostUrl.isNullOrEmpty() || debugHostUrl.isNullOrEmpty() || foxHostUrl.isNullOrEmpty()) {
        throw IllegalStateException(
            "Please configure 'releaseHostUrl,debugHostUrl,foxHostUrl' related information in the 'local.properties',refer to README.md"
        )
    }
    return arrayOf(releaseHostUrl, debugHostUrl, foxHostUrl)
}
