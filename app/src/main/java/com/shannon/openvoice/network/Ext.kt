package com.shannon.openvoice.network

import com.shannon.android.lib.exception.XException
import com.shannon.openvoice.model.BaseResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.network
 * @ClassName:      Ext
 * @Description:     java类作用描述
 * @Author:         czhen
 * @CreateDate:     2022/7/26 14:12
 */

inline fun <reified T> Observable<BaseResponse<T>>.convert2Code(): Observable<Int> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map {
            Timber.d("errorCode = ${it.code}")
            if (it.code != 200) {
                throw XException(it.code, it.msg)
            }
            return@map it.code
        }
}

inline fun <reified T : Any> Observable<BaseResponse<T>>.convert(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map {
            if (it.code != 200 || it.data == null) {
                throw XException(it.code, it.msg)
            }
            Timber.d("errorCode = ${it.code}; data = ${it.data}")
            return@map it.data
        }
}

val apiService: ApiService by lazy { NetworkModule.instance.api() }