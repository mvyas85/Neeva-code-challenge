package com.neeva.neevabrowser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.neeva.neevabrowser.R
import com.neeva.neevabrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    fun startWebSearch(view: View) {
        when(view.id) {
            R.id.btnSearch -> startWebSearchForUrl(binding.etInputUrl.text.toString())
            R.id.ivAmazon -> startWebSearchForUrl("https://www.amazon.com/")
            R.id.ivNeeva -> startWebSearchForUrl("https://www.neeva.com/")
            R.id.ivYoutube -> startWebSearchForUrl("https://www.youtube.com/")
            R.id.ivLinkedin -> startWebSearchForUrl("https://www.linkedin.com/")
            R.id.ivNetflix -> startWebSearchForUrl("https://www.netflix.com/")
            R.id.ivFacebook -> startWebSearchForUrl("https://www.facebook.com/")
        }
    }

    private fun startWebSearchForUrl(url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}