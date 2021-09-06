package com.neeva.neevabrowser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * This is FragmentStateAdapter which loads 5 count of tabs in viewpager
 * When Adapter is loaded it will add calling URL for the first Tab and
 * other tab do not need to open same url, then can be blank.
 */
class BrowserPagerAdapter(fa: FragmentActivity, initialTabUrl: String?): FragmentStateAdapter(fa){

    private val url : String? = initialTabUrl

    override fun getItemCount(): Int {
        return 5 // Hardcoded number of Tabs
    }

    override fun createFragment(position: Int): Fragment {
        val mFragment = WebViewFragment()
        if(position == 0) { // Only open url in first tab,
            val mArgs = Bundle()
            mArgs.putString("url_string", url)
            mFragment.arguments = mArgs
        }
        return mFragment
    }
}