package com.shannon.android.lib.components

import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import com.shannon.android.lib.R
import com.shannon.android.lib.databinding.DialogLoadingBinding
import com.shannon.android.lib.extended.inflate

/**
 *
 * @ClassName:      LoadingDialog
 * @Description:     Loading
 * @Author:         czhen
 */
class LoadingDialog(context: Context) : Dialog(context, R.style.TransparentDialog) {

    private val binding by inflate<DialogLoadingBinding>()


    init {
        binding
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setOnKeyListener { _, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    dismiss()
                    return@setOnKeyListener true
                }
            }
            return@setOnKeyListener super.onKeyDown(keyCode, event)
        }
    }
}