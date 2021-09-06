package com.neeva.neevabrowser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.neeva.neevabrowser.databinding.ActivityWebActivityBinding

/**
 * Main welcome screen which shows user different options either to enter URL in
 * search bar or provides with easy icon to initiate the search.
 */
class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val url = intent.getStringExtra("url")
        with(binding.viewPager) {
            adapter = BrowserPagerAdapter(this@WebViewActivity, url)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            run { tab.text = "Tab $position" }
        }.attach()
    }
}