package com.shannon.android.lib.base.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * @ClassName:      BaseViewModel
 * @Description:     java类作用描述
 * @Author:         czhen
 */
abstract class BaseViewModel : ViewModel(), ModelEvent {

    /**
     * 响应错误码
     */
    val responseErrorLiveData = MutableLiveData<Int>()

    /**
     * 弹出等待框
     */
    val showLoadingLiveData = MutableLiveData<Boolean>()

    /**
     * 取消等待框
     */
    val dismissLoadingLiveData = MutableLiveData<Boolean>()

    /**
     * 保存订阅的任务，在ViewModel销毁时如果任务没有执行完毕就给取消掉
     */
    private val disposableMap = ConcurrentHashMap<String, Disposable>()

    override fun onCleared() {
        super.onCleared()
        for (item in disposableMap.values) {
            if (!item.isDisposed) {
                item.dispose()
            }
        }
        disposableMap.clear()
    }

    /**
     * 绑定观察者
     * @param onNext Function1<T, Unit> 接收到消息的回调
     * @return ViewObserver<T>
     */
    protected fun <T> bindObserver(showLoading: Boolean, onNext: (T) -> Unit) =
            ViewObserver<T>(showLoading, this, onSubscribeNext = onNext)

    /**
     *  绑定观察者
     * @param onNext Function1<T, Unit> 接收到消息的回调
     * @param onError Function1<Throwable, Unit> 任务异常的回调
     * @return ViewObserver<T>
     */
    protected fun <T> bindObserver(
            showLoading: Boolean,
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit
    ) =
            ViewObserver<T>(showLoading, this, onSubscribeNext = onNext, onSubscribeError = onError)


    protected inline fun <reified T> Observable<T>.funSubscribe(
            showLoading: Boolean = true,
            noinline onNext: (T) -> Unit
    ) {
        subscribe(bindObserver(showLoading, onNext))
    }

    protected inline fun <reified T> Observable<T>.funSubscribe(
            showLoading: Boolean = true,
            noinline onError: (Throwable) -> Unit = {},
            noinline onNext: (T) -> Unit
    ) {
        subscribe(bindObserver(showLoading, onNext, onError))
    }

    protected inline fun <reified T> Observable<T>.funSubscribeNotLoading(
            noinline onError: (Throwable) -> Unit = {},
            noinline onNext: (T) -> Unit
    ) {
        subscribe(bindObserver(false, onNext, onError))
    }

    /**
     * 以ViewModel中的方法名为键，将任务请求保存起来，保证每一次请求之前的请求
     * 没有执行完毕的情况下，将之前的请求任务取消掉
     * @param tag String
     * @param disposable Disposable
     */
    private fun saveDisposable(tag: String, disposable: Disposable) {
        if (disposableMap.containsKey(tag)) {
            disposableMap[tag]?.dispose()
        }
        disposableMap[tag] = disposable
    }

    override fun saveDisposable(disposable: Disposable) {
        val stacks = Throwable().stackTrace
        repeat(stacks.size) {
            if (TextUtils.equals(stacks[it].className, this@BaseViewModel.javaClass.name)) {
                saveDisposable(stacks[it].methodName, disposable)
                return
            }
        }
    }

    override fun showLoading() {
        showLoadingLiveData.postValue(true)
    }

    override fun dismissLoading() {
        dismissLoadingLiveData.postValue(true)
    }

    override fun responseError(errorCode: Int) {
        responseErrorLiveData.postValue(errorCode)
    }
}