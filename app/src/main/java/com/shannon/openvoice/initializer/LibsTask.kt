package com.shannon.openvoice.initializer

import com.shannon.android.lib.util.ToastUtil
import com.shannon.openvoice.FunApplication
import com.shannon.openvoice.util.PreferencesUtil

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.initializer
 * @ClassName:      LibsTask
 * @Description:     一些三方库的初始化，对于比较耗时的Lib应该独立Task
 * @Author:         czhen
 * @CreateDate:     2022/7/20 15:37
 */
class LibsTask : FunTask() {

    override fun run() {
        ToastUtil.init(FunApplication.getInstance())

    }
}