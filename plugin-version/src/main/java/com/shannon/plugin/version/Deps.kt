package com.shannon.plugin.version

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.plugin.version
 * @ClassName:      Deps
 * @Description:     java类作用描述
 * @Author:         czhen
 * @CreateDate:     2022/7/18 13:59
 */
object Deps {

    object AndroidX {
        private const val activityVersion = "1.4.0"
        private const val fragmentVersion = "1.4.1"
        private const val recyclerviewVersion = "1.2.1"
        private const val appcompatVersion = "1.4.1"
        private const val materialVersion = "1.5.0"
        private const val constraintLayoutVersion = "2.1.3"
        private const val coreKtxVersion = "1.7.0"
        private const val multidexVersion = "2.0.1"
        private const val mediaVersion = "1.4.3"
        const val coreKtx = "androidx.core:core-ktx:$coreKtxVersion"
        const val activity = "androidx.activity:activity-ktx:$activityVersion"
        const val fragment = "androidx.fragment:fragment-ktx:$fragmentVersion"
        const val recyclerview = "androidx.recyclerview:recyclerview:$recyclerviewVersion"
        const val appcompat = "androidx.appcompat:appcompat:$appcompatVersion"
        const val material = "com.google.android.material:material:$materialVersion"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
        const val multidex = "androidx.multidex:multidex:$multidexVersion"
        const val media = "androidx.media:media:$mediaVersion"
    }

    object Test {
        private const val testExtVersion = "1.1.2"
        private const val testEspressoVersion = "3.3.0"
        private const val junitVersion = "4.12"
        const val testExt = "androidx.test.ext:junit:$testExtVersion"
        const val testEspresso = "androidx.test.espresso:espresso-core:$testEspressoVersion"
        const val junit = "junit:junit:$junitVersion"
    }

    object Glide {
        private const val glideVersion = "4.13.2"
        const val glide = "com.github.bumptech.glide:glide:$glideVersion"
        const val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"
    }

    object Http {
        private const val retrofitVersion = "2.9.0"
        private const val okhttpVersion = "4.10.0"
        private const val gsonVersion = "2.9.0"
        private const val rxJavaVersion = "3.1.5"
        private const val rxAndroidVersion = "3.0.0"
        private const val rxKotlinVersion = "3.0.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val converterScalars = "com.squareup.retrofit2:converter-scalars:$retrofitVersion"
        const val converterGson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        const val adapterRxjava = "com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion"
        const val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion"
        const val okhttpLogger = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
        const val gson = "com.google.code.gson:gson:$gsonVersion"
        const val rxJava = "io.reactivex.rxjava3:rxjava:$rxJavaVersion"
        const val rxAndroid = "io.reactivex.rxjava3:rxandroid:$rxAndroidVersion"
        const val rxKotlin = "io.reactivex.rxjava3:rxkotlin:$rxKotlinVersion"
    }

    object Libs {
        private const val byRecyclerViewVersion = "1.3.2"
        private const val permissionVersion = "1.6.4"
        private const val bannerVersion = "2.1.0"
        private const val cosVersion = "5.7.1"
        private const val timberVersion = "5.0.1"
        private const val startupVersion = "1.0.2"
        private const val immersionBarVersion = "3.2.2"
        private const val mmkvVersion = "1.2.13"
        private const val eventBusVersion = "3.3.1"
        private const val autoSizeVersion = "1.2.1"
        const val byRecyclerView = "com.github.youlookwhat:ByRecyclerView:$byRecyclerViewVersion"
        const val permissionX = "com.guolindev.permissionx:permissionx:$permissionVersion"
        const val banner = "com.youth.banner:banner:$bannerVersion"
        const val cosCloud = "com.qcloud.cos:cos-android-lite:$cosVersion"
        const val timber = "com.jakewharton.timber:timber:$timberVersion"
        const val startup = "com.github.Caij:DGAppStartup:$startupVersion"
        const val immersionBar = "com.geyifeng.immersionbar:immersionbar:$immersionBarVersion"
        const val mmkv = "com.tencent:mmkv:$mmkvVersion"
        const val eventBus = "org.greenrobot:eventbus:$eventBusVersion"
        const val autoSize = "com.github.JessYanCoding:AndroidAutoSize:v$autoSizeVersion"
    }

    object ExoPlayer{
        private const val playerVersion = "2.16.0"
        const val playerCore ="com.google.android.exoplayer:exoplayer-core:$playerVersion"
        const val playerUi="com.google.android.exoplayer:exoplayer-ui:$playerVersion"
        const val playerSession ="com.google.android.exoplayer:extension-mediasession:$playerVersion"

    }
}