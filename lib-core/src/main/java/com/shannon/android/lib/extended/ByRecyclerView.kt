package com.shannon.android.lib.extended

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DefaultItemAnimator
import com.shannon.android.lib.components.EmptyDefaultLayout
import me.jingbin.library.ByRecyclerView
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter
import me.jingbin.library.adapter.BaseByViewHolder

/**
 *
 * @ClassName:      ByRecyclerView
 * @Description:     java类作用描述
 * @Author:         czhen
 */

fun ByRecyclerView.bindEmptyView(
    @DrawableRes resId: Int,
    @StringRes promptStrId: Int,
    @StringRes buttonStrId: Int = 0,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    click: () -> Unit = {}
) {
    setEmptyView(EmptyDefaultLayout(context).apply {
        setImageResource(resId)
        setPromptText(promptStrId)
        setButtonText(buttonStrId)
        setButtonClick(click)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    })
    isLoadMoreEnabled = false
}

fun ByRecyclerView.removeEmptyView(loadMoreEnabled: Boolean = true) {
    if (isStateViewEnabled)
        setEmptyViewEnabled(false)
    if (!isLoadMoreEnabled)
        isLoadMoreEnabled = loadMoreEnabled
}


inline fun <reified T, reified K : BaseByViewHolder<T>> BaseByRecyclerViewAdapter<T, K>.addMoreData(
    data: List<T>
) {
    addData(data)
    if (data.isEmpty()) {
        recyclerView.loadMoreEnd()
    } else {
        recyclerView.loadMoreComplete()
    }
}

fun ByRecyclerView.clearAnimations() {
    if (itemAnimator is DefaultItemAnimator)
        (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

}

fun ByRecyclerView.isFail() {
    if (isRefreshing) {
        isRefreshing = false
    }
    if (isLoadingMore) {
        loadMoreFail()
    }
}