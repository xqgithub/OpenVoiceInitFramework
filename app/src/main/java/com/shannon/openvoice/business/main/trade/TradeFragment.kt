package com.shannon.openvoice.business.main.trade

import com.shannon.android.lib.base.activity.KBaseFragment
import com.shannon.android.lib.base.viewmodel.EmptyViewModel
import com.shannon.openvoice.databinding.FragmentTradeBinding
import timber.log.Timber

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.business.main.trade
 * @ClassName:      TradeFragment
 * @Description:     交易
 * @Author:         czhen
 * @CreateDate:     2022/7/25 16:52
 */
class TradeFragment : KBaseFragment<FragmentTradeBinding, EmptyViewModel>() {

    companion object {
        fun newInstance(): TradeFragment {
            return TradeFragment()
        }
    }

    override fun onInit() {

    }

    override fun onLazyInit() {
        Timber.d("onLazyInit")
    }
}