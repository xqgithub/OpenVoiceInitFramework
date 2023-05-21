package com.shannon.openvoice.business.main.home

import com.shannon.android.lib.base.activity.KBaseFragment
import com.shannon.android.lib.base.viewmodel.EmptyViewModel
import com.shannon.openvoice.databinding.FragmentHomeBinding
import timber.log.Timber

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.business.main.home
 * @ClassName:      HomeFragment
 * @Description:     关注
 * @Author:         czhen
 * @CreateDate:     2022/7/25 16:46
 */
class HomeFragment : KBaseFragment<FragmentHomeBinding, EmptyViewModel>() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onInit() {
    }

    override fun onLazyInit() {
        Timber.d("onLazyInit")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }
}