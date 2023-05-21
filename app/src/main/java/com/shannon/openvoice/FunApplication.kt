package com.shannon.openvoice

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.caij.app.startup.Config
import com.caij.app.startup.DGAppStartup
import com.shannon.android.lib.util.ThemeUtil
import com.shannon.android.lib.util.ToastUtil
import com.shannon.openvoice.initializer.*
import com.shannon.openvoice.util.PreferencesUtil

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice
 * @ClassName:      FunApplication
 * @Description:     Application
 * @Author:         czhen
 * @CreateDate:     2022/7/20 15:24
 */
class FunApplication : Application() {

    companion object {
        private lateinit var instance: Application

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        loadAppConfig()


    }

    private fun loadAppConfig() {
        val startupConfig = Config()
        startupConfig.isStrictMode = BuildConfig.DEBUG
        val startupBuilder = DGAppStartup.Builder()
        startupBuilder.add(ThemeTask())
        startupBuilder.add(LibsTask())
        startupBuilder.add(TimberTask())
        startupBuilder.setConfig(startupConfig)
            .setExecutorService(ThreadManager.getInstance().WORK_EXECUTOR)
            .addTaskListener(MonitorTaskListener("initializerTag", true))
            .create().start().await()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}