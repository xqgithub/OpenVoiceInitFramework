package com.shannon.plugin.version

import java.io.File
import java.text.SimpleDateFormat

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.plugin.version
 * @ClassName:      AndroidConfig
 * @Description:     java类作用描述
 * @Author:         czhen
 * @CreateDate:     2022/7/15 17:54
 */
object AndroidConfig {

    const val appName = "OpenVoice"
    const val applicationId = "com.shannon.openvoice"
    const val compileSdkVersion = 31
    const val minSdkVersion = 21
    const val targetSdkVersion = 31
    const val versionCode = 1
    const val versionName = "0.1.0"

    val baleDate: String = SimpleDateFormat("MMddHHmm").format(System.currentTimeMillis())

}