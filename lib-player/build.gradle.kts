import com.shannon.plugin.version.*
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.shannon.plugin.version")
}

android {
    compileSdk =  AndroidConfig.compileSdkVersion

    defaultConfig {
        minSdk =  AndroidConfig.minSdkVersion
        targetSdk =  AndroidConfig.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.media)

    implementation(Deps.ExoPlayer.playerCore)
    implementation(Deps.ExoPlayer.playerUi)
    implementation(Deps.ExoPlayer.playerSession)

    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.testExt)
    androidTestImplementation(Deps.Test.testEspresso)
}