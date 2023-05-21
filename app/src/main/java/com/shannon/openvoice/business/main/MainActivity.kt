package com.shannon.openvoice.business.main

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.shannon.android.lib.base.activity.KBaseActivity
import com.shannon.android.lib.base.adapter.SimpleActivityPageAdapter
import com.shannon.android.lib.base.viewmodel.EmptyViewModel
import com.shannon.android.lib.extended.addArray
import com.shannon.android.lib.extended.singleClick
import com.shannon.android.lib.util.ThemeUtil
import com.shannon.openvoice.R
import com.shannon.openvoice.business.main.creation.CreationFragment
import com.shannon.openvoice.business.main.federated.FederatedFragment
import com.shannon.openvoice.business.main.home.HomeFragment
import com.shannon.openvoice.business.main.notification.NotificationFragment
import com.shannon.openvoice.business.main.trade.TradeFragment
import com.shannon.openvoice.components.MainNavigationView
import com.shannon.openvoice.databinding.ActivityMainBinding
import me.jessyan.autosize.AutoSizeConfig
import timber.log.Timber

class MainActivity : KBaseActivity<ActivityMainBinding, EmptyViewModel>(),
    MainNavigationView.OnTabSelectedListener {

    private val fragments = arrayListOf<Fragment>()
    private val mainTitle = arrayListOf(
        R.string.title_home,
        R.string.title_public_local,
        R.string.title_trade,
        R.string.title_creation,
        R.string.title_notifications
    )

    override fun onInit() {
        ImmersionBar.with(this)
            .navigationBarColorInt(ThemeUtil.getColor(this, R.attr.toolbarBackground))
            .init()
        Timber.d("MainActivity init")

        setTitleText(mainTitle[0])
        initFragments()
        binding.run {
            viewPager.adapter = SimpleActivityPageAdapter(this@MainActivity, fragments)
            viewPager.isUserInputEnabled = false
            viewPager.offscreenPageLimit = fragments.size
            navigationView.setOnTabSelectedListener(this@MainActivity)
            userAvatarView.singleClick { drawerLayout.open() }
        }



    }

    private fun initFragments() {
        fragments.addArray(
            HomeFragment.newInstance(),
            FederatedFragment.newInstance(),
            TradeFragment.newInstance(),
            CreationFragment.newInstance(),
            NotificationFragment.newInstance()
        )
    }


    override fun onTabSelected(position: Int) {
        Timber.d("onTabSelected --> $position")
        setTitleText(mainTitle[position])
        binding.viewPager.setCurrentItem(position, false)
    }

    override fun onTabUnselected(position: Int) {
        Timber.d("onTabUnselected --> $position")
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isOpen){
            binding.drawerLayout.close()
        }else{
            moveTaskToBack(true)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                onBackPressed()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}