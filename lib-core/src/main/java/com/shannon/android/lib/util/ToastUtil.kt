package com.shannon.android.lib.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
 *
 * @ClassName:      ToastUtil
 * @Description:     java类作用描述
 * @Author:         czhen
 */
object ToastUtil {

    private lateinit var sApplicationContext: Context

    fun init(applicationContext: Context) {
        sApplicationContext = applicationContext
    }

    /**
     * 短时间显示Toast【居下】
     * @param message 显示的内容-字符串*/
    fun showToast(message: String) {
        createToast().apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            setText(message)
            show()
        }
    }

    /**
     * 短时间显示Toast【居中】
     * @param message 显示的内容-字符串*/
    fun showCenter(message: String) {
        createToast().apply {
            setGravity(Gravity.CENTER, 0, 0)
            setText(message)
            show()
        }
    }

    @SuppressLint("ShowToast")
    private fun createToast(): Toast {
        return Toast.makeText(sApplicationContext, "", Toast.LENGTH_SHORT)
    }

}