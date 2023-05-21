package com.shannon.android.lib.extended

import android.view.View

/**
 *
 * @ClassName:      Extended
 * @Description:     java类作用描述
 * @Author:         czhen
 */

inline fun <reified T : Any> MutableList<T>.addArray(vararg obj: T) {
    obj.forEach {
        this.add(it)
    }
}

fun View.gone() {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun View.visible() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}
