package com.shannon.openvoice.initializer

import android.util.Log
import com.shannon.android.lib.util.ThemeUtil
import com.shannon.openvoice.FunApplication
import com.shannon.openvoice.util.PreferencesUtil

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.initializer
 * @ClassName:      ThemeTask
 * @Description:     主题设置
 * @Author:         czhen
 * @CreateDate:     2022/7/22 10:14
 */
class ThemeTask : FunTask() {

    override fun isMustRunMainThread(): Boolean {
        return true
    }

    override fun run() {
        PreferencesUtil.initialize(FunApplication.getInstance())
        val themeMode = PreferencesUtil.getString(
            PreferencesUtil.Constant.THEME_MODE,
            ThemeUtil.APP_THEME_DEFAULT
        )
        ThemeUtil.setAppNightMode(themeMode)
    }
}