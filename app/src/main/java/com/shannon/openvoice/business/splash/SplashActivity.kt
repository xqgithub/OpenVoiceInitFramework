package com.shannon.openvoice.business.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shannon.openvoice.business.main.MainActivity

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.business.splash
 * @ClassName:      SplashActivity
 * @Description:     启动页
 * @Author:         czhen
 * @CreateDate:     2022/7/21 14:38
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(MainActivity.newIntent(this))
        onBackPressed()
    }
}