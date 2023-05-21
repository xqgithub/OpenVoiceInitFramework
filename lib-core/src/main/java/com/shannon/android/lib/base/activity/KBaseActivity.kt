package com.shannon.android.lib.base.activity

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.shannon.android.lib.R
import com.shannon.android.lib.base.viewmodel.BaseViewModel
import com.shannon.android.lib.components.LoadingDialog
import com.shannon.android.lib.extended.singleClick
import com.shannon.android.lib.util.ThemeUtil
import timber.log.Timber
import java.lang.reflect.ParameterizedType


abstract class KBaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    protected val context by lazy { this }
    private val loadingDialog by lazy { LoadingDialog(context) }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val type = javaClass.genericSuperclass as ParameterizedType
        val modelProvider = ViewModelProvider(this)
        val clazz0 = type.actualTypeArguments[0] as Class<VB>
        val method = clazz0.getMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as VB
        setContentView(binding.root)

        val clazz1 = type.actualTypeArguments[1] as Class<VM>
        viewModel = modelProvider.get(clazz1)
        bindViewModelEvent()

        defaultBindToolbar()
        onInit()
    }

    abstract fun onInit()


    private fun bindViewModelEvent() {
        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).apply {
                responseErrorLiveData.observe(context) {
                    handleResponseError(it)
                }
                showLoadingLiveData.observe(context) {
                    this@KBaseActivity.showLoading()
                }
                dismissLoadingLiveData.observe(context) {
                    this@KBaseActivity.dismissLoading()
                }
            }
        }
    }


    protected open fun handleResponseError(errorCode: Int) {

    }

    open fun showLoading() {
        loadingDialog.run {
            if (!isShowing) show()
        }
    }

    open fun dismissLoading() {
        loadingDialog.run {
            if (isShowing) dismiss()
        }
    }

    private fun defaultBindToolbar() {
        val toolbarId = ThemeUtil.getAttrResourceId(context, R.attr.toolbarId)
        val toolbar = findViewById<View>(toolbarId)
        toolbar?.run { bindToolbar(this) }

        val navigationButtonId = ThemeUtil.getAttrResourceId(context, R.attr.navigationButtonId)
        val navigationButton = findViewById<View>(navigationButtonId)
        navigationButton?.run { singleClick { onBackPressed() } }
    }

    protected fun setTitleText(@StringRes resId: Int) {
        val titleViewId =
            ThemeUtil.getAttrResourceId(context, R.attr.titleViewId)
        val titleView = binding.root.findViewById<TextView>(titleViewId)
        titleView?.run { setText(resId) }
    }


    protected open fun bindToolbar(titleBar: View) {
        bindToolbar(titleBar, AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES)
    }

    protected open fun bindToolbar(titleBar: View, isDarkFont: Boolean) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .titleBar(titleBar)
            .statusBarDarkFont(isDarkFont)
            .navigationBarColorInt(ThemeUtil.getColor(this,android.R.attr.windowBackground))
            .navigationBarDarkIcon(isDarkFont)
            .init()
    }
}

