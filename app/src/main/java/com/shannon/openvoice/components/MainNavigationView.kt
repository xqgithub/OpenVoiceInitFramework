package com.shannon.openvoice.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.shannon.android.lib.extended.addArray
import com.shannon.android.lib.extended.gone
import com.shannon.android.lib.extended.inflateBinding
import com.shannon.android.lib.extended.singleClick
import com.shannon.openvoice.R
import com.shannon.openvoice.databinding.LayoutMainNavigationBinding

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.components
 * @ClassName:      MainNavigationView
 * @Description:     首页底部导航
 * @Author:         czhen
 * @CreateDate:     2022/7/25 9:48
 */
class MainNavigationView : FrameLayout {

    private val viewBinding = inflateBinding<LayoutMainNavigationBinding>(this)
    private val tabIconList = arrayListOf<View>()
    private val tabTextList = arrayListOf<View>()

    private var currentPosition = -1
    private var onTabSelectedListener: OnTabSelectedListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        viewBinding.run {
            addView(root)
            tabIconList.addArray(
                homeTabIcon,
                federatedTabIcon,
                tradeTabIcon,
                creationTabIcon,
                notificationTabIcon
            )
            tabTextList.addArray(
                homeTabText,
                federatedTabText,
                tradeTabText,
                creationTabText,
                notificationTabText
            )
            homeLayout.singleClick(this@MainNavigationView::onClick)
            federatedLayout.singleClick(this@MainNavigationView::onClick)
            tradeLayout.singleClick(this@MainNavigationView::onClick)
            creationLayout.singleClick(this@MainNavigationView::onClick)
            notificationLayout.singleClick(this@MainNavigationView::onClick)
        }

        hideTabText()
        selectTab(0)

    }

    private fun onClick(v: View) {
        when (v.id) {
            R.id.homeLayout -> selectTab(0)
            R.id.federatedLayout -> selectTab(1)
            R.id.tradeLayout -> selectTab(2)
            R.id.creationLayout -> selectTab(3)
            R.id.notificationLayout -> selectTab(4)
        }
    }

    private fun selectTab(newPosition: Int) {
        if (currentPosition != newPosition) {
            setSelectedTabView(newPosition)
            if (currentPosition >= 0) onTabSelectedListener?.onTabUnselected(currentPosition)
            onTabSelectedListener?.onTabSelected(newPosition)
            currentPosition = newPosition
        }
    }

    private fun setSelectedTabView(position: Int) {
        tabIconList.withIndex().forEach {
            it.value.isSelected = it.index == position
            it.value.isActivated = it.index == position
        }
        tabTextList.withIndex().forEach {
            it.value.isSelected = it.index == position
            it.value.isActivated = it.index == position
        }
    }

    private fun hideTabText() {
        tabTextList.forEach { it.gone() }
    }

    fun setOnTabSelectedListener(tabSelectedListener: OnTabSelectedListener) {
        this.onTabSelectedListener = tabSelectedListener
    }

    interface OnTabSelectedListener {
        fun onTabSelected(position: Int)
        fun onTabUnselected(position: Int)
    }

}