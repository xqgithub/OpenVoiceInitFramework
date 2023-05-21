package com.shannon.openvoice.business.main.creation

import com.shannon.android.lib.base.activity.KBaseFragment
import com.shannon.android.lib.base.viewmodel.EmptyViewModel
import com.shannon.openvoice.databinding.FragmentCreationBinding
import timber.log.Timber

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.business.main.creation
 * @ClassName:      CreationFragment
 * @Description:     声音模型
 * @Author:         czhen
 * @CreateDate:     2022/7/25 16:50
 */
class CreationFragment : KBaseFragment<FragmentCreationBinding, EmptyViewModel>() {

    companion object {
        fun newInstance(): CreationFragment {
            return CreationFragment()
        }
    }

    override fun onInit() {

    }

    override fun onLazyInit() {
        Timber.d("onLazyInit")
    }
}