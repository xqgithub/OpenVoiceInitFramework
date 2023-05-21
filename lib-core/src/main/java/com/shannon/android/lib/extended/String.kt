package com.shannon.android.lib.extended

/**
 *
 * @ClassName:      String
 * @Description:     java类作用描述
 * @Author:         czhen
 */

fun String?.nonNull(defaultValue: String = ""): String {
    if (this == null) return defaultValue
    return this
}