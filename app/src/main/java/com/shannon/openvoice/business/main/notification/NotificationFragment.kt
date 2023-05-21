package com.shannon.openvoice.business.main.notification

import com.shannon.android.lib.base.activity.KBaseFragment
import com.shannon.android.lib.base.viewmodel.EmptyViewModel
import com.shannon.openvoice.databinding.FragmentNotificationBinding
import timber.log.Timber

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.business.main.notification
 * @ClassName:      NotificationFragment
 * @Description:     通知
 * @Author:         czhen
 * @CreateDate:     2022/7/25 16:51
 */
class NotificationFragment:KBaseFragment<FragmentNotificationBinding,EmptyViewModel>() {

    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }

    override fun onInit() {

    }

    override fun onLazyInit() {
        Timber.d("onLazyInit")
    }
}