package com.neeva.neevabrowser.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.neeva.neevabrowser.R
import com.neeva.neevabrowser.databinding.FragmentWebviewBinding
import com.neeva.neevabrowser.viewmodel.BrowsHistoryViewModel

/**
 * Ech TabItem is consists of this fragment
 * primary responsibility of this class is to
 * 1. set URL in first tab when it launches from mainActivity
 * 2. set current state of each tab's canGoForward flag and set it from ViewModel
 * 3. set user's navigation experience (forward/back)
 * 4. each tab have pull to refresh experience
 */
class WebViewFragment: Fragment() {
    private lateinit var binding: FragmentWebviewBinding
    private lateinit var viewModel: BrowsHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebviewBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadUrl = arguments?.getString("url_string", null)

        viewModel = ViewModelProviders.of(this)
                    .get(BrowsHistoryViewModel::class.java)
        viewModel.canGoForward.observe(requireActivity(), {
            if (it) {
                binding.searchBox.ivForward.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.forward_enable
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.searchBox.ivForward.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.forward_diable
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        })

        with(binding){
            loadUrl?.let {
                loadUrl(it)
                searchBox.etInputUrl.setText(loadUrl)
            }
            webView.settings.javaScriptEnabled = true
            webView.settings.builtInZoomControls = true
            webView.settings.displayZoomControls = false
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    searchProgress.visibility = View.VISIBLE;
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    viewModel.setCanGoForward(webView.canGoForward())
                    searchBox.etInputUrl.setText(url)
                    refreshPage.isRefreshing = false
                    searchProgress.visibility = View.GONE;
                }
            }
            searchBox.etInputUrl.setOnEditorActionListener(TextView.OnEditorActionListener { et, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    loadUrl(et.text.toString())
                    et.clearFocus()
                    hideKeyboard()
                }
                false
            })
            searchBox.ivHome.setOnClickListener { onHomeClicked() }
            searchBox.ivBack.setOnClickListener { onBackPressed() }
            searchBox.ivForward.setOnClickListener { onForwardPressed() }
            refreshPage.setOnRefreshListener { webView.reload() }
        }
    }

    private fun onForwardPressed() {
        if (binding.webView.canGoForward()) {
            binding.webView.goForward()
        }
    }

    private fun onBackPressed() {
        if (binding.webView.canGoBack()){
            binding.webView.goBack()
        }
        else{
            activity?.onBackPressed()
        }
    }

    private fun onHomeClicked() {
        activity?.let{
            val intent = Intent(it, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            it.startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun loadUrl(url: String){
        if (URLUtil.isValidUrl(url)){
            binding.webView.loadUrl(url)
        }
        else{
            binding.webView.loadUrl("https://www.google.com/search?q=$url")
        }
    }

   fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}